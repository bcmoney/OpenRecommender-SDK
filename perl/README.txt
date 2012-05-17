To build as a Perl module, use:
  h2xs -b 5.10.1 -AX OpenRecommender

Then copy/paste the code from OpenRecommender.pm into the generated Perl module in /lib/ sub-directory

Update documentation in Perl Module (if necesary)

Compile the generated "make" file:
  perl Makefile.PL

Then run the project packaging "make" command:
  make
NOTE: If trying to build on Windows, use "nmake" which comes with Microsoft Visual C++ Express/Pro, see: 
http://johnbokma.com/perl/make-for-windows.html


Finally, install the module in your local Perl "/site/lib" repository using:
  make install


-----------------------------------------------------------
For more information on how to build your own module, see:
http://members.pcug.org.au/~rcook/PerlModule_HOWTO.html