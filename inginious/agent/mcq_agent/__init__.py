# -*- coding: utf-8 -*-
#
# This file is part of INGInious. See the LICENSE and the COPYRIGHTS files for
# more information about the licensing of this file.
import asyncio
import gettext
import logging

from inginious import get_root_path
from inginious.agent import Agent, CannotCreateJobException
from inginious.common.course_factory import create_factories
from inginious.common.messages import BackendNewJob, BackendKillJob
from inginious.common.tasks_problems import MultipleChoiceProblem, MatchProblem


class MCQAgent(Agent):
    def __init__(self, context, backend_addr, friendly_name, concurrency, tasks_filesystem, user_manager=None, sac=None):
        """
        :param context: ZeroMQ context for this process
        :param backend_addr: address of the backend (for example, "tcp://127.0.0.1:2222")
        :param friendly_name: a string containing a friendly name to identify agent
        :param tasks_filesystem: FileSystemProvider to the course/tasks
        """
        super().__init__(context, backend_addr, friendly_name, concurrency, tasks_filesystem)
        self._logger = logging.getLogger("inginious.agent.mcq")

        # Create a course factory
        problem_types = {problem_type.get_type(): problem_type for problem_type in [MultipleChoiceProblem, MatchProblem]}

        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info(("User m ", user_manager))

        course_factory, _ = create_factories(tasks_filesystem, problem_types, user_manager=user_manager, sac=sac)  # TODO maybe add user_manager
        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info(("After"))
        self.course_factory = course_factory

        # Init gettext
        languages = ["en", "fr"]
        self._translations = {
            lang: gettext.translation('messages', get_root_path() + '/agent/mcq_agent/i18n', [lang]) for lang in languages
        }

    @property
    def environments(self):
        return {"mcq": {"id": "mcq", "created": 0}}

    async def new_job(self, msg: BackendNewJob):
        try:
            self._logger.info("Received request for jobid %s", msg.job_id)
            task = self.course_factory.get_task(msg.course_id, msg.task_id)
        except asyncio.CancelledError:
            raise
        except Exception as e:
            from inginious.common.log import get_course_logger
            get_course_logger("Custom").info(("Error in new job (mcq_agent) ", e, msg.course_id, msg.task_id))
            self._logger.error("Task %s/%s not available on this agent", msg.course_id, msg.task_id)
            raise CannotCreateJobException("Task is not available on this agent")

        language = msg.inputdata.get("@lang", "")
        translation = self._translations.get(language, gettext.NullTranslations())
        _ = translation.gettext

        result, need_emul, text, problems, error_count, mcq_error_count = task.check_answer(msg.inputdata, language)

        internal_messages = {
            "_wrong_answer_multiple": _("Wrong answer. Make sure to select all the valid possibilities"),
            "_wrong_answer": _("Wrong answer"),
            "_correct_answer": _("Correct answer"),
        }

        for key, (p_result, messages) in problems.items():
            messages = [internal_messages[message] if message in internal_messages else message for message in messages]
            problems[key] = (p_result, "\n\n".join(messages))

        if need_emul:
            self._logger.warning("Task %s/%s is not a pure MCQ but has env=MCQ", msg.course_id, msg.task_id)
            raise CannotCreateJobException("Task wrongly configured as a MCQ")

        if error_count != 0:
            text.append(_("You have {} wrong answer(s).").format(error_count))
        if mcq_error_count != 0:
            text.append("\n\n" + _("Among them, you have {} invalid answers in the multiple choice questions").format(mcq_error_count))

        nb_subproblems = len(task.get_problems())
        grade = 100.0 * float(nb_subproblems - error_count) / float(nb_subproblems)

        await self.send_job_result(msg.job_id, ("success" if result else "failed"), "\n".join(text), grade, problems, {}, {}, "", None)

    async def kill_job(self, message: BackendKillJob):
        pass
