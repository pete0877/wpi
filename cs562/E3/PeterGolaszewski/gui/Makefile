## NOTE: THIS HAS NOT BEEN UPDATED AND MAY NOT WORK

TABLECLASSFILES= \
	gui/TableReader.class \
	gui/TableBean.class \
	gui/TableBeanBeanInfo.class \
	gui/TableEventObject.class  \
	gui/TableElement.class \
	gui/TableRegion.class \
	gui/TableRegionEditor.class \
	gui/TableListener.class

TABLEDATAFILES= \
	gui/TableBeanIcon16.gif \
	gui/TableBeanIcon32.gif

JARS      = tb.jar 
META-INF  = /tmp/META-INF
MANIFEST  = $(META-INF)/MANIFEST.MF

all: $(JARS)

# Create a JAR file with a suitable manifest.

tb.jar: $(TABLECLASSFILES) $(TABLEDATAFILES)
	mkdir $(META-INF)
	echo "Name: gui/TableBean.class" >> $(MANIFEST)
	echo "Java-Bean: True" >> $(MANIFEST)
	jar cfm tb.jar $(MANIFEST) $(TABLECLASSFILES) $(TABLEDATAFILES)
	@/bin/rm -rf $(META-INF)

# Must be tab separators
%.class: %.java
	export CLASSPATH; CLASSPATH=.:$(ADAPTCLASSES) ; \
	javac $<

clean:
	/bin/rm -f  $(TABLECLASSFILES)
	/bin/rm -f tb.jar

