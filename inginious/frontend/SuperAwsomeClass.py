
class SuperAwesomeClass:
    def __init__(self, username):
        self._username = username
        self.print("Init")

    def print(self, string=''):
        from inginious.common.log import get_course_logger
        get_course_logger("Custom").info((string, str(self)))

    def __str__(self):
        return "%s(%s)" % (self.__class__, self.username)

    @property
    def username(self):
        #self.print("Get")
        return self._username

    def set_username(self, username):
        self._username = username
        self.print("Set")
