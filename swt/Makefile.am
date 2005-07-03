extract_zip_files:
	unzip -u swt*.zip src.zip "*swt*" -x swt.jar
	unzip -u src.zip "org/*"

swt_libraries = $(shell ls lib* *dll 2>/dev/null | grep -v "iriverter")
swt_libraries_extension = $(shell echo $(swt_libraries) | sed 's/.*\.//' | uniq)
swt_iriverter_libraries = $(shell ls *iriverter* 2>/dev/null)
swt_jar_source_files = $(shell find org -iname "*.java")
swt_jar_class_files = $(swt_jar_source_files:.java=.class)

move_libraries: extract_zip_files
	rename .$(swt_libraries_extension) -iriverter.$(swt_libraries_extension) $(swt_libraries)
	for file in `grep -Rl "System\.loadLibrary" org`; do sed 's/System\.loadLibrary[ ]*([\"a-zA-Z]*/& + \"-iriverter\"/g' $$file > $$file.new && mv -f $$file.new $$file; done

if BUILD_GCJ
$(swt_jar_class_files): %.class: %.java
	$(GCJ) -C $<
else
$(swt_jar_class_files): %.class: %.java
	$(JAVAC) $<
endif

all_swt_jar_class_files = $(shell find . -iname "*.class" | sed 's/\$$/\\\$$/g')

swt.jar: move_libraries
	make $(swt_jar_class_files)
	$(JAR) cf $@ $(all_swt_jar_class_files)

CLEANFILES = \
	$(swt_iriverter_libraries) \
	org \
	src.zip

clean-generic:
	-test -z "$(CLEANFILES)" || rm -rf $(CLEANFILES)
