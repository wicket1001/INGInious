# -*- coding: utf-8 -*-
#
# This file is part of INGInious. See the LICENSE and the COPYRIGHTS files for
# more information about the licensing of this file.

""" Tasks' problems """
import gettext
import sys
import re
from abc import ABCMeta, abstractmethod

from inginious.common.base import id_checker


class Problem(object, metaclass=ABCMeta):
    """Basic problem """

    @classmethod
    @abstractmethod
    def get_type(cls):
        """ Returns the type of the problem """
        return None

    @abstractmethod
    def input_is_consistent(self, task_input, default_allowed_extension, default_max_size):
        """ Check if an input for this problem is consistent. Return true if this is case, false else """
        return False

    @abstractmethod
    def input_type(self):
        """ Indicates if problem input type """
        return str

    @abstractmethod
    def check_answer(self, task_input, language):
        """
            Check the answer. Returns four values:
            the first is either True, False or None, indicating respectively that the answer is valid, invalid, or need to be sent to VM
            the second is the error message assigned to the task, if any (unused for now)
            the third is the error message assigned to this problem, if any
            the fourth is the number of errors in MCQ; should be zero when not a MCQ.
        """
        return True, None, None, 0

    @classmethod
    @abstractmethod
    def get_text_fields(cls):
        """ Returns a dict whose keys are the keys of content dict
        and val is True if value of content[key] is human-readable text """
        return {"name": True}

    def get_id(self):
        """ Get the id of this problem """
        return self._id

    def get_task(self):
        """ Get the task containing this problem """
        return self._task

    def get_name(self, language=None):
        """ Get the name of this problem """
        return self.gettext(language, self._name) if self._name else ""

    def get_original_content(self):
        """ Get a dict fully describing this sub-problem """
        return dict(self._original_content)

    def __init__(self, task, problemid, content, translations=None):
        if not id_checker(problemid):
            raise Exception("Invalid problem _id: " + problemid)

        self._translations = translations
        self._id = problemid
        self._task = task
        self._name = content['name'] if "name" in content else ""
        self._original_content = content

    @classmethod
    def parse_problem(self, problem_content):
        if "limit" in problem_content:
            try:
                problem_content["limit"] = int(problem_content["limit"])
            except:
                del problem_content["limit"]
        return problem_content

    def gettext(self, language, *args, **kwargs):
        translation = self._translations.get(language, gettext.NullTranslations())
        return translation.gettext(*args, **kwargs)


class CodeProblem(Problem):
    """Code problem"""

    def __init__(self, task, problemid, content, translations=None):
        Problem.__init__(self, task, problemid, content, translations)
        self._header = content['header'] if "header" in content else ""
        self._optional = content.get("optional", False)

        if re.match(r'[a-z0-9\-_\.]+$', content.get("language", ""), re.IGNORECASE):
            self._language = content.get("language", "")
        elif content.get("language", ""):
            raise Exception("Invalid language " + content["language"])
        else:
            self._language = "plain"

        self._default = content.get("default", "")

    def input_type(self):
        return str

    @classmethod
    def get_type(cls):
        return "code"

    def check_answer(self, _, __):
        return None, None, None, 0

    def input_is_consistent(self, task_input, default_allowed_extension, default_max_size):
        try:
            if not self.get_id() in task_input:
                return False

            # do not allow empty answers
            if len(task_input[self.get_id()]) == 0:
                if self._optional:
                    task_input[self.get_id()] = ""
                else:
                    return False
        except:
            return False
        return True

    @classmethod
    def parse_problem(self, problem_content):
        return Problem.parse_problem(problem_content)

    @classmethod
    def get_text_fields(cls):
        return Problem.get_text_fields().update({"header": True})


class CodeSingleLineProblem(CodeProblem):
    """Code problem with a single line of input"""

    @classmethod
    def get_type(cls):
        return "code_single_line"


class FileProblem(Problem):
    """File upload Problem"""

    def __init__(self, task, problemid, content, translations=None):
        Problem.__init__(self, task, problemid, content, translations)
        self._header = content['header'] if "header" in content else ""
        self._max_size = content.get("max_size", None)
        self._allowed_exts = content.get("allowed_exts", None)

    def input_type(self):
        return dict

    def check_answer(self, _, __):
        return None, None, None, 0

    @classmethod
    def get_type(cls):
        return "file"

    @classmethod
    def parse_problem(self, problem_content):
        problem_content = Problem.parse_problem(problem_content)
        if "allowed_exts" in problem_content:
            if problem_content["allowed_exts"] == "":
                del problem_content["allowed_exts"]
            else:
                problem_content["allowed_exts"] = problem_content["allowed_exts"].split(',')

        if "max_size" in problem_content:
            try:
                problem_content["max_size"] = int(problem_content["max_size"])
            except:
                del problem_content["max_size"]
        return problem_content

    def input_is_consistent(self, task_input, default_allowed_extension, default_max_size):
        if not str(self.get_id()) in task_input:
            return False
        try:
            if not task_input[self.get_id()]["filename"].endswith(tuple(self._allowed_exts or default_allowed_extension)):
                return False

            if sys.getsizeof(task_input[self.get_id()]["value"]) > (self._max_size or default_max_size):
                return False
        except:
            return False
        return True

    @classmethod
    def get_text_fields(cls):
        return CodeProblem.get_text_fields().update({"header": True})


class MultipleChoiceProblem(Problem):
    """Multiple choice problems"""

    def __init__(self, task, problemid, content, translations=None):
        super(MultipleChoiceProblem, self).__init__(task, problemid, content, translations)
        self._header = content['header'] if "header" in content else ""
        self._multiple = content.get("multiple", False)
        if "choices" not in content or not isinstance(content['choices'], list):
            raise Exception("Multiple choice problem " + problemid + " does not have choices or choices are not an array")
        good_choices = []
        bad_choices = []
        for index, choice in enumerate(content["choices"]):
            data = {"index": index}
            if "text" not in choice:
                raise Exception("A choice in " + problemid + " does not have text")
            data['text'] = choice["text"]
            data['feedback'] = choice.get('feedback')
            if choice.get('valid', False):
                data['valid'] = True
                good_choices.append(data)
            else:
                data['valid'] = False
                bad_choices.append(data)

        if len(good_choices) == 0:
            raise Exception("Problem " + problemid + " does not have any valid answer")

        self._limit = 0
        if "limit" in content and isinstance(content['limit'], int) and content['limit'] >= 0 and (not self._multiple or content['limit'] >= \
                len(good_choices) or content['limit'] == 0):
            self._limit = content['limit']
        elif "limit" in content:
            raise Exception("Invalid limit in problem " + problemid)

        self._centralize = content.get("centralize", False)

        self._error_message = content.get("error_message", None)
        self._success_message = content.get("success_message", None)

        self._choices = good_choices + bad_choices

    @classmethod
    def get_type(cls):
        return "multiple_choice"

    def allow_multiple(self):
        """ Returns true if this multiple choice problem allows checking multiple answers """
        return self._multiple

    def get_choice_with_index(self, index):
        """ Return the choice with index=index """
        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info((index, self._choices))
        for entry in self._choices:
            if entry["index"] == index:
                return entry
        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info(("None"))
        return None

    def input_type(self):
        return list if self._multiple else str

    def input_is_consistent(self, task_input, default_allowed_extension, default_max_size):
        if self.get_id() not in task_input:
            return False
        if self._multiple:
            if not isinstance(task_input[self.get_id()], list):
                return False
            try:  # test conversion to int
                for entry in task_input[self.get_id()]:
                    if self.get_choice_with_index(int(entry)) is None:
                        return False
            except ValueError:
                return False
        else:
            try:  # test conversion to int
                if self.get_choice_with_index(int(task_input[self.get_id()])) is None:
                    return False
            except ValueError:
                return False
        return True

    def check_answer(self, task_input, language):
        valid = True
        msgs = []
        invalid_count = 0

        def log(text):
            from inginious.common.log import get_course_logger
            get_course_logger("Custom").info(text)

        if self._multiple:
            for choice in self._choices:
                if choice["valid"] and not choice["index"] in task_input[self.get_id()] and not str(choice["index"]) in task_input[self.get_id()]:
                    valid = False
                    invalid_count += 1
                elif not choice["valid"] and (choice["index"] in task_input[self.get_id()] or str(choice["index"]) in task_input[self.get_id()]):
                    valid = False
                    invalid_count += 1
            for i in task_input[self.get_id()]:
                feedback = self.get_choice_with_index(int(i))["feedback"]
                if feedback is not None:
                    msgs.append(self.gettext(language, feedback))
        else:
            from inginious.common.log import get_course_logger
            get_course_logger("Custom").info(("ID: ", task_input, self.get_id()))
            choice = self.get_choice_with_index(int(task_input[self.get_id()]))
            valid = choice["valid"]
            if not valid:
                invalid_count += 1
            if choice["feedback"] is not None:
                msgs.append(self.gettext(language, choice["feedback"]))

        if not valid:
            if self._error_message is not None:
                msgs = [self.gettext(language, self._error_message)] + msgs
            elif not self._centralize:
                msgs = ["_wrong_answer_multiple" if self._multiple else "_wrong_answer"] + msgs

            if len(msgs) != 0:
                return False, None, msgs, invalid_count
            else:
                return False, None, None, invalid_count

        if self._success_message is not None:
            msgs = [self.gettext(language, self._success_message)] + msgs

        if len(msgs) != 0:
            return True, None, msgs, 0
        else:
            return True, None, None, 0

    @classmethod
    def parse_problem(self, problem_content):
        problem_content = Problem.parse_problem(problem_content)
        # store boolean fields as booleans
        for field in ["optional", "multiple", "centralize"]:
            if field in problem_content:
                problem_content[field] = True

        if "choices" in problem_content:
            problem_content["choices"] = [val for _, val in
                                          sorted(iter(problem_content["choices"].items()), key=lambda x: int(x[0]))]
            for choice in problem_content["choices"]:
                if "valid" in choice:
                    choice["valid"] = True
                if "feedback" in choice and choice["feedback"].strip() == "":
                    del choice["feedback"]

        for message in ["error_message", "success_message"]:
            if message in problem_content and problem_content[message].strip() == "":
                del problem_content[message]

        return problem_content

    @classmethod
    def get_text_fields(cls):
        result = Problem.get_text_fields()
        result.update({"header": True, "success_message": True, "error_message": True, "choices": [{"text": True, "feedback": True}]})
        return result


class MatchProblem(Problem):
    """Display an input box and check that the content is correct"""

    def __init__(self, task, problemid, content, translations=None):
        super(MatchProblem, self).__init__(task, problemid, content, translations)
        self._header = content['header'] if "header" in content else ""
        if not "answer" in content:
            raise Exception("There is no answer in this problem with type==match")
        self._answer = str(content["answer"])

    @classmethod
    def get_type(cls):
        return "match"

    def input_is_consistent(self, task_input, default_allowed_extension, default_max_size):
        return self.get_id() in task_input

    def input_type(self):
        return str

    def check_answer(self, task_input, language):
        if task_input[self.get_id()].strip() == self._answer:
            return True, None, ["_correct_answer"], 0
        else:
            return False, None, ["_wrong_answer"], 0

    @classmethod
    def parse_problem(self, problem_content):
        return Problem.parse_problem(problem_content)

    @classmethod
    def get_text_fields(cls):
        return Problem.get_text_fields().update({"header": True})
