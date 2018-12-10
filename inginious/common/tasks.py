# -*- coding: utf-8 -*-
#
# This file is part of INGInious. See the LICENSE and the COPYRIGHTS files for
# more information about the licensing of this file.

""" Task """
import gettext

from inginious.common.base import id_checker


class Task(object):
    """ Contains the data for a task """

    def __init__(self, course, taskid, content, task_fs, hook_manager, task_problem_types, seed=''): # Added seed in constructor
        """
            Init the task. course is a Course object, taskid the task id, and content is a dictionnary containing the data needed to initialize the Task object.
            If init_data is None, the data will be taken from the course tasks' directory.
        """
        self._course = course
        self._taskid = taskid
        self._fs = task_fs
        self._hook_manager = hook_manager
        self._data = content
        self._environment = self._data.get('environment', None)

        # Response is HTML
        self._response_is_html = self._data.get("responseIsHTML", False)

        # Limits
        self._limits = {"time": 20, "memory": 1024, "disk": 1024}
        if "limits" in self._data:
            try:
                self._limits['time'] = int(self._data["limits"].get("time", 20))
                self._limits['hard_time'] = int(self._data["limits"].get("hard_time", 3 * self._limits['time']))
                self._limits['memory'] = int(self._data["limits"].get("memory", 1024))
                self._limits['disk'] = int(self._data["limits"].get("disk", 1024))

                if self._limits['time'] <= 0 or self._limits['hard_time'] <= 0 or self._limits['memory'] <= 0 or self._limits['disk'] <= 0:
                    raise Exception("Invalid limit")
            except:
                raise Exception("Invalid limit")

        if "problems" not in self._data:
            raise Exception("Tasks must have some problems descriptions")

        # from inginious.common.log import get_course_logger
        # get_course_logger("Custom").info("INFO")
        self._random_question = {"choose": 0, "whole": -1, "takeout": -1}
        # choose 1 = wird zufällig ausgewählt, choose 0 = alle werden angezeigt
        # whole = gesamte Anzahl der Fragen dieser Sektion
        # takeout = Anzahl der Fragen die davon genommen werden
        if "random_question" in self._data:
            try:
                self._random_question["choose"] = int(self._data["random_question"].get("choose", 0))
                self._random_question["whole"] = int(self._data["random_question"].get("whole", -1))
                self._random_question["takeout"] = int(self._data["random_question"].get("takeout", -1))
            except:
                raise Exception("This is a self programmed expansion. The Identifiers where not valid. Invalid Random Question.")

        # Network access in grading container?
        self._network_grading = self._data.get("network_grading", False)

        # i18n
        self._translations = {}
        translations_fs = self._fs.from_subfolder("$i18n")
        if translations_fs.exists():
            for f in translations_fs.list(folders=False, files=True, recursive=False):
                lang = f[0:len(f) - 3]
                if translations_fs.exists(lang + ".mo"):
                    self._translations[lang] = gettext.GNUTranslations(translations_fs.get_fd(lang + ".mo"))
                else:
                    self._translations[lang] = gettext.NullTranslations()

        # Check all problems
        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info(("Seed:", seed))
        from random import Random
        x = "{}#{}".format(taskid, seed)
        rand = Random(x)

        self._problems = []
        temp_problems = []
        for problemid in self._data['problems']:
            temp_problems.append(self._create_task_problem(problemid, self._data['problems'][problemid], task_problem_types))

        try:
            if self._random_question["choose"] == 1 and temp_problems:
                for i in range(self._random_question["takeout"]):
                    chosen = rand.randint(0, len(temp_problems) - 1)
                    self._problems.append(temp_problems.pop(chosen))
            else:
                self._problems = temp_problems
        except Exception as error:
            raise Exception("The problem set to permute has a flaw.")
        '''
        self._problems = []
        for problemid in self._data['problems']:
            self._problems.append(self._create_task_problem(problemid, self._data['problems'][problemid], task_problem_types))
        '''

        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info(("HERE it goes in common tasks"))

        # Order
        self._order = int(self._data.get('order', -1))

    def gettext(self, language, *args, **kwargs):
        translation = self._translations.get(language, gettext.NullTranslations())
        return translation.gettext(*args, **kwargs)

    def input_is_consistent(self, task_input, default_allowed_extension, default_max_size):
        """ Check if an input for a task is consistent. Return true if this is case, false else """
        for problem in self._problems:
            if not problem.input_is_consistent(task_input, default_allowed_extension, default_max_size):
                return False
        return True

    def get_order(self):
        """ Get the position of this task in the course """
        return self._order

    def get_environment(self):
        """ Returns the environment in which the agent have to launch this task"""
        return self._environment

    def get_id(self):
        """ Get the id of this task """
        return self._taskid

    def get_problems(self):
        """ Get problems contained in this task """
        return self._problems

    def get_course_id(self):
        """ Return the courseid of the course that contains this task """
        return self._course.get_id()

    def get_course(self):
        """ Return the course that contains this task """
        return self._course

    def get_limits(self):
        """ Return the limits of this task """
        vals = self._hook_manager.call_hook('task_limits', course=self.get_course(), task=self, default=self._limits)
        return vals[0] if len(vals) else self._limits

    def get_random_question(self):
        vals = self._hook_manager.call_hook('task_random_question', course=self.get_course(), task=self, default=self._random_question)
        return vals[0] if len(vals) else self._random_question

    def allow_network_access_grading(self):
        """ Return True if the grading container should have access to the network """
        vals = self._hook_manager.call_hook('task_network_grading', course=self.get_course(), task=self, default=self._network_grading)
        return vals[0] if len(vals) else self._network_grading

    def get_response_type(self):
        """ Returns the method used to parse the output of the task: HTML or rst """
        return "HTML" if self._response_is_html else "rst"

    def get_fs(self):
        """ Returns a FileSystemProvider which points to the folder of this task """
        return self._fs

    def check_answer(self, task_input, language):
        """
            Verify the answers in task_input. Returns six values
            1st: True the input is **currently** valid. (may become invalid after running the code), False else
            2nd: True if the input needs to be run in the VM, False else
            3rd: Main message, as a list (that can be join with \n or <br/> for example)
            4th: Problem specific message, as a dictionnary (tuple of result/text)
            5th: Number of subproblems that (already) contain errors. <= Number of subproblems
            6th: Number of errors in MCQ problems. Not linked to the number of subproblems
        """
        valid = True
        need_launch = False
        main_message = []
        problem_messages = {}
        error_count = 0
        multiple_choice_error_count = 0
        for problem in self._problems:
            problem_is_valid, problem_main_message, problem_s_messages, problem_mc_error_count = problem.check_answer(task_input, language)
            if problem_is_valid is None:
                need_launch = True
            elif problem_is_valid == False:
                error_count += 1
                valid = False
            if problem_main_message is not None:
                main_message.append(problem_main_message)
            if problem_s_messages is not None:
                problem_messages[problem.get_id()] = (("success" if problem_is_valid else "failed"), problem_s_messages)
            multiple_choice_error_count += problem_mc_error_count
        return valid, need_launch, main_message, problem_messages, error_count, multiple_choice_error_count

    def _create_task_problem(self, problemid, problem_content, task_problem_types):
        """Creates a new instance of the right class for a given problem."""
        # Basic checks
        if not id_checker(problemid):
            raise Exception("Invalid problem _id: " + problemid)
        if problem_content.get('type', "") not in task_problem_types:
            raise Exception("Invalid type for problem " + problemid)

        return task_problem_types.get(problem_content.get('type', ""))(self, problemid, problem_content, self._translations)
