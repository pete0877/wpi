ADAPTCLASSES = $(ADAPT_HOME)/classes

CLASSFILES= \
	spreadsheet/Spreadsheet.class \
	spreadsheet/SpreadsheetBeanInfo.class \
	spreadsheet/SpreadsheetEventObject.class \
	spreadsheet/SpreadsheetListener.class \
	spreadsheet/Expression.class \
	spreadsheet/Function.class \
	spreadsheet/FunctionException.class \
	spreadsheet/AddFunction.class \
	spreadsheet/MinusFunction.class \
	spreadsheet/MultiplyFunction.class \
	spreadsheet/DivideFunction.class \
	spreadsheet/PiFunction.class \
	spreadsheet/AverageFunction.class \
	spreadsheet/Node.class \
	spreadsheet/CellRegion.class \
	spreadsheet/Cell.class

DATAFILES= \
	spreadsheet/SpreadsheetIcon16.gif \
	spreadsheet/SpreadsheetIcon32.gif

JARDIR =  jars

JARFILE= $(JARDIR)/spreadsheet.jar

META-INF=/tmp/META-INF
MANIFEST=$(META-INF)/MANIFEST.MF

all: $(JARFILE)

# Create a JAR file with a suitable manifest.

$(JARFILE): $(CLASSFILES) $(DATAFILES)
	echo "Name: spreadsheet/Spreadsheet.class" >> manifest.tmp
	echo "Java-Bean: True" >> manifest.tmp
	jar cfm $(JARFILE) manifest.tmp $(CLASSFILES) $(DATAFILES)
	@/bin/rm manifest.tmp

%.class: %.java
	export CLASSPATH; CLASSPATH=.:$(ADAPTCLASSES) ; \
	javac $<

clean:
	/bin/rm -f $(CLASSFILES)
	/bin/rm -f $(JARFILE)

