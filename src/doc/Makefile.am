SUBDIRS = html images

html: *.xml
	docbook2html -o html -V '%use-id-as-filename%' index.xml
