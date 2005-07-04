AC_INIT(src/net/sourceforge/iriverter/ConverterUI.java)

VERSION=0.14
AC_SUBST(VERSION)

AM_INIT_AUTOMAKE(iriverter, $VERSION)

AM_PROG_GCJ
AC_DISABLE_SHARED
AC_PROG_LIBTOOL
AC_PROG_INSTALL

case "$host_os" in
	cygwin* | mingw*)
		windows="yes"
	;;
esac

AM_CONDITIONAL(BUILD_WIN, test "$windows" = "yes")

AC_ARG_WITH(gcj, [  --with-gcj              build JunePimp using the GCJ compiler], gcj="$withval", gcj="no")
AM_CONDITIONAL(BUILD_GCJ, test "$gcj" = "yes")

if test "$gcj" = "yes"; then
	if test "$GCJ" = ""; then
		AC_MSG_ERROR([Can't find "gcj" in your PATH])
	fi

	if test "$windows" = "yes"; then
		AC_PATH_PROG(WINDRES, "$host_alias"-windres)
		if test "$WINDRES" = ""; then
			AC_MSG_ERROR([Can't find "$host_alias-windres" in your PATH])
		fi
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

AC_OUTPUT([Makefile doc/Makefile doc/html/Makefile doc/images/Makefile src/Makefile swt/Makefile])
