AC_INIT(src/net/sourceforge/iriverter/ConverterUI.java)

VERSION=0.14
AC_SUBST(VERSION)

AM_INIT_AUTOMAKE(iriverter, $VERSION)

AM_PROG_GCJ
AC_DISABLE_STATIC
AC_PROG_LIBTOOL
AC_PROG_INSTALL

AC_ARG_WITH(gcj, [  --with-gcj              build JunePimp using the GCJ compiler], gcj="$withval", gcj="no")
AM_CONDITIONAL(BUILD_GCJ, test "$gcj" = "yes")

if test "$gcj" = "yes"; then
	if test "$GCJ" = ""; then
		AC_MSG_ERROR([Can't find "gcj" in your PATH])
	fi
else
	if test "$JAVAC" = ""; then
		AC_MSG_ERROR([Can't find "javac" in your PATH])
	fi
fi

AC_PATH_PROG(FASTJAR, fastjar)
AC_PATH_PROG(GCJ_JAR, gcj-jar)
AC_PATH_PROG(JAR, jar)

if test "$FASTJAR" != ""; then
	JAR="$FASTJAR"
elif test "$GCJ_JAR" != ""; then
	JAR="$GCJ_JAR"
fi

if test "$JAR" = ""; then
	AC_MSG_ERROR([Can't find "fastjar", "gcj-jar", or "jar" in your PATH])
fi

AC_ARG_WITH(swt, [  --with-swt=JAR          the jar contating the SWT library], swt="$withval", swt="no")

if test "$swt" = "no"; then
	AC_MSG_ERROR([Can't find "swt.jar"])
else
	SWT_JAR="$swt"
	AC_SUBST(SWT_JAR)
fi

AC_OUTPUT([Makefile src/Makefile])
