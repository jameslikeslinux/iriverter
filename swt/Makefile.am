if BUILD_GCJ
lib_LTLIBRARIES = libswt-iriverter.la
libswt_iriverter_la_SOURCES = swt-iriverter.jar
libswt_iriverter_la_GCJFLAGS = -fjni
else
swt_iriverter_jardir = $(pkgdatadir)/lib
swt_iriverter_jar_DATA = swt-iriverter.jar
endif

.extract_zip_files: swt*.zip
	unzip -u swt*.zip src.zip "*swt*" -x swt.jar 
	unzip -u src.zip "org/*"
	touch .extract_zip_files

swt_libraries = $(shell ls lib* *dll 2>/dev/null | grep -v "iriverter")
swt_libraries_extension = $(shell echo $(swt_libraries) | sed 's/.*\.//' | uniq)

swt_iriverter_libraries = $(shell ls *iriverter* 2>/dev/null | grep -v "swt.iriverter")
swt_iriverter_librariesdir = $(libdir)
swt_iriverter_libraries_DATA = $(swt_iriverter_libraries)

swt_iriverter_jar_source_files = $(shell find org -iname "*.java" 2>/dev/null)
swt_iriverter_jar_class_files = $(swt_iriverter_jar_source_files:.java=.class)

.fix_files: .extract_zip_files
	for file in `grep -Rl "System\.loadLibrary" org`; do sed 's/System\.loadLibrary[ ]*([\"a-zA-Z]*/& + \"-iriverter\"); \/\//g' $$file > $$file.new && mv -f $$file.new $$file; done
	sed 's/ error(/ browserError(/g' org/eclipse/swt/browser/Browser.java > org/eclipse/swt/browser/Browser.java.new && mv -f org/eclipse/swt/browser/Browser.java.new org/eclipse/swt/browser/Browser.java
	for file in `grep -Rl "Browser\.error" org`; do sed 's/Browser\.error/Browser\.browserError/g' $$file > $$file.new && mv -f $$file.new $$file; done
	touch .fix_files

.move_libraries: .extract_zip_files
	rename .$(swt_libraries_extension) -iriverter.$(swt_libraries_extension) $(swt_libraries)
	touch .move_libraries

if BUILD_GCJ
$(swt_iriverter_jar_class_files): %.class: %.java
	-$(GCJ) -C -classpath . $<
else
$(swt_iriverter_jar_class_files): %.class: %.java
	-$(JAVAC) -classpath . $<
endif

all_swt_iriverter_jar_class_files = $(shell find . -iname "*.class" | sed 's/\$$/\\\$$/g')

build_jar: $(swt_iriverter_jar_class_files)
	$(JAR) cf swt-iriverter.jar $(all_swt_iriverter_jar_class_files)

swt-iriverter.jar: .move_libraries .fix_files
	make build_jar

CLEANFILES = \
	.extract_zip_files \
	.fix_files \
	.move_libraries \
	$(swt_iriverter_libraries) \
	org \
	src.zip \
	swt-iriverter.jar

clean-generic:
	-test -z "$(CLEANFILES)" || rm -rf $(CLEANFILES)
