SUBDIRS = doc profiles launcher

if BUILD_GCJ
bin_PROGRAMS = iriverter
endif

if !BUILD_WIN
icondir = $(datadir)/pixmaps
icon_DATA = org/thestaticvoid/iriverter/icons/iriverter.png

launcherdir = $(datadir)/applications
launcher_DATA = iriverter.desktop
endif

iriverter_source_files = \
	org/thestaticvoid/iriverter/FrameRateDialog.java \
	org/thestaticvoid/iriverter/DVD.java \
	org/thestaticvoid/iriverter/SingleVideoAdapter.java \
	org/thestaticvoid/iriverter/DVDProgressDialog.java \
	org/thestaticvoid/iriverter/ConverterUI.java \
	org/thestaticvoid/iriverter/JoinVideos.java \
	org/thestaticvoid/iriverter/Directory.java \
	org/thestaticvoid/iriverter/AboutDialog.java \
	org/thestaticvoid/iriverter/ProgressDialog.java \
	org/thestaticvoid/iriverter/BitrateDialog.java \
	org/thestaticvoid/iriverter/TabItemControl.java \
	org/thestaticvoid/iriverter/Converter.java \
	org/thestaticvoid/iriverter/DVDAdapter.java \
	org/thestaticvoid/iriverter/ManualSplitInfo.java \
	org/thestaticvoid/iriverter/MPlayerInfo.java \
	org/thestaticvoid/iriverter/HelpBrowser.java \
	org/thestaticvoid/iriverter/AudioSyncDialog.java \
	org/thestaticvoid/iriverter/DVDTitleInfo.java \
	org/thestaticvoid/iriverter/DirectoryInfo.java \
	org/thestaticvoid/iriverter/DVDInfo.java \
	org/thestaticvoid/iriverter/SingleVideoInfo.java \
	org/thestaticvoid/iriverter/ManualSplitAdapter.java \
	org/thestaticvoid/iriverter/SingleVideo.java \
	org/thestaticvoid/iriverter/ListOfFiles.java \
	org/thestaticvoid/iriverter/Chapters.java \
	org/thestaticvoid/iriverter/DVDInfoReader.java \
	org/thestaticvoid/iriverter/OverwriteDialog.java \
	org/thestaticvoid/iriverter/ConverterOptions.java \
	org/thestaticvoid/iriverter/Dimensions.java \
	org/thestaticvoid/iriverter/VolumeFilter.java \
	org/thestaticvoid/iriverter/ProfileFilter.java \
	org/thestaticvoid/iriverter/OutputVideoInfo.java \
	org/thestaticvoid/iriverter/JoinVideosInfo.java \
	org/thestaticvoid/iriverter/Config.java \
	org/thestaticvoid/iriverter/Profile.java \
	org/thestaticvoid/iriverter/Bitrate.java \
	org/thestaticvoid/iriverter/regex/Pattern.java \
	org/thestaticvoid/iriverter/regex/Matcher.java \
	org/thestaticvoid/iriverter/regex/PatternSyntaxException.java \
	org/thestaticvoid/iriverter/Mark.java \
	org/thestaticvoid/iriverter/ChapterDialog.java \
	org/thestaticvoid/iriverter/DirectoryAdapter.java \
	org/thestaticvoid/iriverter/VideoFileFilter.java \
	org/thestaticvoid/iriverter/AutomaticallySplitDialog.java \
	org/thestaticvoid/iriverter/VolumeDialog.java \
	org/thestaticvoid/iriverter/regexp/REToken.java \
	org/thestaticvoid/iriverter/regexp/RE.java \
	org/thestaticvoid/iriverter/regexp/RETokenEndSub.java \
	org/thestaticvoid/iriverter/regexp/UncheckedRE.java \
	org/thestaticvoid/iriverter/regexp/RESyntax.java \
	org/thestaticvoid/iriverter/regexp/REMatchEnumeration.java \
	org/thestaticvoid/iriverter/regexp/CharIndexedCharArray.java \
	org/thestaticvoid/iriverter/regexp/CharIndexedString.java \
	org/thestaticvoid/iriverter/regexp/RETokenChar.java \
	org/thestaticvoid/iriverter/regexp/RETokenOneOf.java \
	org/thestaticvoid/iriverter/regexp/RETokenWordBoundary.java \
	org/thestaticvoid/iriverter/regexp/RETokenRepeated.java \
	org/thestaticvoid/iriverter/regexp/RETokenLookAhead.java \
	org/thestaticvoid/iriverter/regexp/REMatch.java \
	org/thestaticvoid/iriverter/regexp/REException.java \
	org/thestaticvoid/iriverter/regexp/CharIndexedInputStream.java \
	org/thestaticvoid/iriverter/regexp/RETokenPOSIX.java \
	org/thestaticvoid/iriverter/regexp/CharIndexedStringBuffer.java \
	org/thestaticvoid/iriverter/regexp/RETokenEnd.java \
	org/thestaticvoid/iriverter/regexp/RETokenStart.java \
	org/thestaticvoid/iriverter/regexp/RETokenRange.java \
	org/thestaticvoid/iriverter/regexp/REFilterInputStream.java \
	org/thestaticvoid/iriverter/regexp/RETokenBackRef.java \
	org/thestaticvoid/iriverter/regexp/RETokenAny.java \
	org/thestaticvoid/iriverter/regexp/CharIndexed.java \
	org/thestaticvoid/iriverter/ProgressDialogInfo.java \
	org/thestaticvoid/iriverter/MencoderStreamParser.java \
	org/thestaticvoid/iriverter/ManualSplit.java

iriverter_icons = \
	org/thestaticvoid/iriverter/icons/iriverter.png \
	org/thestaticvoid/iriverter/icons/convert-24.png \
	org/thestaticvoid/iriverter/icons/dvd-24.png \
	org/thestaticvoid/iriverter/icons/dvd-16.png \
	org/thestaticvoid/iriverter/icons/directory-24.png \
	org/thestaticvoid/iriverter/icons/directory-16.png \
	org/thestaticvoid/iriverter/icons/singlevideo-24.png \
	org/thestaticvoid/iriverter/icons/back-24.png \
	org/thestaticvoid/iriverter/icons/home-24.png \
	org/thestaticvoid/iriverter/icons/forward-24.png \
	org/thestaticvoid/iriverter/icons/singlevideo-16.png

iriverter_resources = \
	gnu/regexp/MessagesBundle_fr.properties \
	gnu/regexp/MessagesBundle.properties

if !BUILD_GCJ
iriverter_jardir = $(pkgdatadir)/lib
iriverter_jar_DATA = iriverter.jar
endif

iriverter_jar_class_files = $(iriverter_source_files:.java=.class)

if BUILD_GCJ
$(iriverter_jar_class_files): %.class: %.java
	$(GCJ) -C -classpath $(SWT_JAR):. $<
else
$(iriverter_jar_class_files): %.class: %.java
	$(JAVAC) -source 1.4 -target 1.4 -classpath $(SWT_JAR):. $<
endif

all_iriverter_jar_class_files = $(shell find . -iname '*.class' | sed 's/\$$/\\\$$/g')

iriverter.jar: $(iriverter_jar_class_files)
	$(JAR) cf $@ $(all_iriverter_jar_class_files) $(iriverter_icons) $(iriverter_resources)

swt.jar: $(SWT_JAR)
	cp -f $< $@

resource.o: resource.rc
	$(WINDRES) -o $@ $<

if BUILD_GCJ
iriverter_SOURCES = iriverter.jar swt.jar
if BUILD_WIN
iriverter_LDFLAGS = -mwindows --main=org.thestaticvoid.iriverter.ConverterUI
iriverter_LDADD = resource.o
else
iriverter_LDFLAGS = --main=org.thestaticvoid.iriverter.ConverterUI
endif
iriverter_GCJFLAGS = -fjni --classpath=$(SWT_JAR)
endif

EXTRA_DIST = \
	$(iriverter_source_files) \
	$(iriverter_icons) \
	$(iriverter_resources) \
	iriverter.desktop \
	iriverter.ico \
	manifest.xml \
	resource.rc

DISTFILES = $(DIST_COMMON) $(EXTRA_DIST)

CLEANFILES = \
	$(all_iriverter_jar_class_files) \
	iriverter.jar \
	swt.jar \
	resource.o
