if BUILD_GCJ
bin_PROGRAMS = iriverter
endif

iriverter_source_files = \
	net/sourceforge/iriverter/FrameRateDialog.java \
	net/sourceforge/iriverter/DVD.java \
	net/sourceforge/iriverter/SingleVideoAdapter.java \
	net/sourceforge/iriverter/DVDProgressDialog.java \
	net/sourceforge/iriverter/ConverterUI.java \
	net/sourceforge/iriverter/JoinVideos.java \
	net/sourceforge/iriverter/Directory.java \
	net/sourceforge/iriverter/AboutDialog.java \
	net/sourceforge/iriverter/ProgressDialog.java \
	net/sourceforge/iriverter/BitrateDialog.java \
	net/sourceforge/iriverter/TabItemControl.java \
	net/sourceforge/iriverter/Converter.java \
	net/sourceforge/iriverter/DVDAdapter.java \
	net/sourceforge/iriverter/ManualSplitInfo.java \
	net/sourceforge/iriverter/MPlayerInfo.java \
	net/sourceforge/iriverter/HelpBrowser.java \
	net/sourceforge/iriverter/AudioSyncDialog.java \
	net/sourceforge/iriverter/DVDTitleInfo.java \
	net/sourceforge/iriverter/DirectoryInfo.java \
	net/sourceforge/iriverter/DVDInfo.java \
	net/sourceforge/iriverter/SingleVideoInfo.java \
	net/sourceforge/iriverter/ManualSplitAdapter.java \
	net/sourceforge/iriverter/SingleVideo.java \
	net/sourceforge/iriverter/ListOfFiles.java \
	net/sourceforge/iriverter/Chapters.java \
	net/sourceforge/iriverter/DVDInfoReader.java \
	net/sourceforge/iriverter/OverwriteDialog.java \
	net/sourceforge/iriverter/ConverterOptions.java \
	net/sourceforge/iriverter/OutputVideoInfo.java \
	net/sourceforge/iriverter/JoinVideosInfo.java \
	net/sourceforge/iriverter/Bitrate.java \
	net/sourceforge/iriverter/regex/Pattern.java \
	net/sourceforge/iriverter/regex/Matcher.java \
	net/sourceforge/iriverter/regex/PatternSyntaxException.java \
	net/sourceforge/iriverter/Mark.java \
	net/sourceforge/iriverter/ChapterDialog.java \
	net/sourceforge/iriverter/DirectoryAdapter.java \
	net/sourceforge/iriverter/VideoFileFilter.java \
	net/sourceforge/iriverter/AutomaticallySplitDialog.java \
	net/sourceforge/iriverter/regexp/REToken.java \
	net/sourceforge/iriverter/regexp/RE.java \
	net/sourceforge/iriverter/regexp/RETokenEndSub.java \
	net/sourceforge/iriverter/regexp/UncheckedRE.java \
	net/sourceforge/iriverter/regexp/RESyntax.java \
	net/sourceforge/iriverter/regexp/REMatchEnumeration.java \
	net/sourceforge/iriverter/regexp/CharIndexedCharArray.java \
	net/sourceforge/iriverter/regexp/CharIndexedString.java \
	net/sourceforge/iriverter/regexp/RETokenChar.java \
	net/sourceforge/iriverter/regexp/RETokenOneOf.java \
	net/sourceforge/iriverter/regexp/RETokenWordBoundary.java \
	net/sourceforge/iriverter/regexp/RETokenRepeated.java \
	net/sourceforge/iriverter/regexp/RETokenLookAhead.java \
	net/sourceforge/iriverter/regexp/REMatch.java \
	net/sourceforge/iriverter/regexp/REException.java \
	net/sourceforge/iriverter/regexp/CharIndexedInputStream.java \
	net/sourceforge/iriverter/regexp/RETokenPOSIX.java \
	net/sourceforge/iriverter/regexp/CharIndexedStringBuffer.java \
	net/sourceforge/iriverter/regexp/RETokenEnd.java \
	net/sourceforge/iriverter/regexp/RETokenStart.java \
	net/sourceforge/iriverter/regexp/RETokenRange.java \
	net/sourceforge/iriverter/regexp/REFilterInputStream.java \
	net/sourceforge/iriverter/regexp/RETokenBackRef.java \
	net/sourceforge/iriverter/regexp/RETokenAny.java \
	net/sourceforge/iriverter/regexp/CharIndexed.java \
	net/sourceforge/iriverter/ProgressDialogInfo.java \
	net/sourceforge/iriverter/MencoderStreamParser.java \
	net/sourceforge/iriverter/ManualSplit.java

iriverter_icons = \
	net/sourceforge/iriverter/icons/convert-24.png \
	net/sourceforge/iriverter/icons/dvd-24.png \
	net/sourceforge/iriverter/icons/dvd-16.png \
	net/sourceforge/iriverter/icons/iriver.gif \
	net/sourceforge/iriverter/icons/directory-24.png \
	net/sourceforge/iriverter/icons/directory-16.png \
	net/sourceforge/iriverter/icons/singlevideo-24.png \
	net/sourceforge/iriverter/icons/back-24.png \
	net/sourceforge/iriverter/icons/iriver-16.gif \
	net/sourceforge/iriverter/icons/home-24.png \
	net/sourceforge/iriverter/icons/forward-24.png \
	net/sourceforge/iriverter/icons/singlevideo-16.png

iriverter_resources = \
	gnu/regexp/MessagesBundle_fr.properties \
	gnu/regexp/MessagesBundle.properties

iriverter_jardir = $(pkgdatadir)/lib
iriverter_jar_DATA = iriverter.jar

iriverter_jar_class_files = $(iriverter_source_files:.java=.class)

if BUILD_GCJ
$(iriverter_jar_class_files): %.class: %.java
	$(GCJ) -C -classpath ../swt/swt-iriverter.jar:. $<
else
$(iriverter_jar_class_files): %.class: %.java
	$(JAVAC) -classpath ../swt/swt-iriverter.jar:. $<
endif

all_iriverter_jar_class_files = $(shell find . -iname '*.class' | sed 's/\$$/\\\$$/g')

iriverter.jar: $(iriverter_jar_class_files)
	$(JAR) cf $@ $(all_iriverter_jar_class_files) $(iriverter_icons) $(iriverter_resources)

if BUILD_GCJ
iriverter_SOURCES = iriverter.jar
iriverter_GCJFLAGS = -fjni
iriverter_LDFLAGS = --main=net.sourceforge.iriverter.ConverterUI
iriverter_LDADD = ../swt/libswt-iriverter.la
endif

CLEANFILES = \
	$(all_iriverter_jar_class_files) \
	iriverter.jar
