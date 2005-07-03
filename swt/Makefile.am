if BUILD_GCJ
lib_LTLIBRARIES = libswt-iriverter.la
libswt_iriverter_la_SOURCES = swt.jar
libswt_iriverter_la_GCJFLAGS = -fjni
endif

extract_zip_files:
	unzip -u swt*.zip src.zip "*swt*" -x swt.jar 
	unzip -u src.zip "org/*"

swt_libraries = $(shell ls lib* *dll 2>/dev/null | grep -v "iriverter")
swt_libraries_extension = $(shell echo $(swt_libraries) | sed 's/.*\.//' | uniq)
swt_iriverter_libraries = $(shell ls *iriverter* 2>/dev/null)
swt_jar_source_files = $(shell find org -iname "*.java" 2>/dev/null)
swt_jar_class_files = $(swt_jar_source_files:.java=.class)

move_libraries: extract_zip_files
	rename .$(swt_libraries_extension) -iriverter.$(swt_libraries_extension) $(swt_libraries)
	for file in `grep -Rl "System\.loadLibrary" org`; do sed 's/System\.loadLibrary[ ]*([\"a-zA-Z]*/& + \"-iriverter\"/g' $$file > $$file.new && mv -f $$file.new $$file; done
	sed 's/ error(/ browserError(/g' org/eclipse/swt/browser/Browser.java > org/eclipse/swt/browser/Browser.java.new && mv -f org/eclipse/swt/browser/Browser.java.new org/eclipse/swt/browser/Browser.java
	for file in `grep -Rl "Browser\.error" org`; do sed 's/Browser\.error/Browser\.browserError/g' $$file > $$file.new && mv -f $$file.new $$file; done

if BUILD_GCJ
$(swt_jar_class_files): %.class: %.java
	-$(GCJ) -C -classpath . $<
else
$(swt_jar_class_files): %.class: %.java
	-$(JAVAC) -classpath . $<
endif

all_swt_jar_class_files = $(shell find . -iname "*.class" | sed 's/\$$/\\\$$/g')

build_jar: $(swt_jar_class_files)
	$(JAR) cf swt.jar $(all_swt_jar_class_files)

swt.jar: move_libraries
	make build_jar

CLEANFILES = \
	$(swt_iriverter_libraries) \
	org \
	src.zip \
	swt.jar

clean-generic:
	-test -z "$(CLEANFILES)" || rm -rf $(CLEANFILES)
