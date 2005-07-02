#!/bin/sh
aclocal \
&& libtoolize --force --copy \
&& autoconf \
&& automake --add-missing --copy
