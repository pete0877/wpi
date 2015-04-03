ADAPTCLASSES = $(ADAPT_HOME)/classes

TEXTCLASSFILES= \
	gui/TextBean.class \
	gui/TextBeanEventObject.class \
	gui/TextBeanListener.class \
	gui/TextBeanBeanInfo.class

TABLECLASSFILES= \
	gui/TableReader.class \
	gui/TableBean.class \
	gui/TableBeanBeanInfo.class \
	gui/TableEventObject.class  \
	gui/TableElement.class \
	gui/TableRegion.class \
	gui/TableRegionEditor.class \
	gui/TableListener.class

TEXTDATAFILES= \
	gui/TextBeanIcon16.gif \
	gui/TextBeanIcon32.gif

TABLEDATAFILES= \
	gui/TableBeanIcon16.gif \
	gui/TableBeanIcon32.gif

JARDIR    = jars
JARS      = $(JARDIR)/table.jar $(JARDIR)/text.jar
META-INF  = /tmp/META-INF
MANIFEST  = $(META-INF)/MANIFEST.MF

all: $(JARS)

# Create a JAR file with a suitable manifest.

$(JARDIR)/table.jar: $(TABLECLASSFILES) $(TABLEDATAFILES)
	mkdir $(META-INF)
	echo "Name: gui/TableBean.class" >> $(MANIFEST)
	echo "Java-Bean: True" >> $(MANIFEST)
	jar cfm $(JARDIR)/table.jar $(MANIFEST) $(TABLECLASSFILES) $(TABLEDATAFILES)
	@/bin/rm -rf $(META-INF)

$(JARDIR)/text.jar: $(TEXTCLASSFILES) $(TEXTDATAFILES)
	mkdir $(META-INF)
	echo "Name: gui/TextBean.class" >> $(MANIFEST)
	echo "Java-Bean: True" >> $(MANIFEST)
	jar cfm $(JARDIR)/text.jar $(MANIFEST) $(TEXTCLASSFILES) $(TEXTDATAFILES)
	@/bin/rm -fr $(META-INF)

# Must be tab separators
%.class: %.java
	export CLASSPATH; CLASSPATH=.:$(ADAPTCLASSES) ; \
	javac $<

clean:
	/bin/rm -f $(TEXTCLASSFILES) $(TABLECLASSFILES)
	/bin/rm -f $(JARDIR)/table.jar $(JARDIR)/text.jar

