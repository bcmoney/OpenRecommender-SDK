To run in Apache Web Server:

1. Download "mod_wsgi":
 http://code.google.com/p/modwsgi/


2. Unzip the file "mod_rewrite.so" to:
 modules/mod_rewrite.so


3. Add the following to link the LoadModule section where other modules are loaded:
 LoadModule rewrite_module modules/mod_rewrite.so


4a. Then add the following just below any CGI declarations:
    # PYTHON
    PassEnv PYTHONPATH
    SetEnv PYTHONUNBUFFERED 1    
    AddHandler wsgi-script .py

-OR-

4b. Change the line that references "cgi-script" to include Python and add just below tell the script interpreter to make automatic file associations to call up the correct compiler:
    AddHandler cgi-script .cgi .py
    ScriptInterpreterSource Registry-Strict



In a Linux/Unix box (4a) worked better, however on Windows 7 64-bit OS (4b) tended to work better but was less encouraged as it didn't take full benefit of "mod_wsgi".
