swt_zip = $(shell ls swt*.zip)
swt_libraries = $(shell unzip -ql $(swt_zip) "*swt*" -x swt.jar | grep -o "[a-z0-9\-]*\..*")
swt_libraries_extension = $(shell echo $(swt_libraries) | sed 's/.*\.//' | uniq)
swt_iriverter_libraries = $(swt_libraries:.$(swt_libraries_extension)=-iriverter.$(swt_libraries_extension))

swt_iriverter_librariesdir = $(pkglibdir)
swt_iriverter_libraries_DATA = $(swt_iriverter_libraries)

$(swt_libraries): $(swt_zip)
	unzip $(swt_zip) $@

$(swt_iriverter_libraries): %-iriverter.$(swt_libraries_extension): %.$(swt_libraries_extension)
	mv -f $< $@

src_zip = $(shell unzip $(swt_zip) src.zip)
swt_jar_source_files = $(shell unzip -ql src.zip "*.java" | grep -o "[/a-zA-Z0-9\-]*\..*";)
swt_jar_class_files = $(swt_jar_source_files:.java=.class)

$(swt_jar_class_files): %.class: %.java
	cat $< | sed 's/System\.loadLibrary[ ]*([\"a-zA-Z]*/& + \"-iriverter\"/g' > $<.new && mv -f $<.new $<
if BUILD_GCJ
	$(GCJ) -C $<
else
	$(JAVAC) $<
endif

all_swt_jar_class_files = $(shell find . -iname "*.class" | sed 's/\$$/\\\$$/g')

swt.jar: $(swt_jar_class_files)
	$(JAR) cf $@ $(all_swt_jar_class_files)

CLEANFILES = \
	$(swt_iriverter_libraries)
