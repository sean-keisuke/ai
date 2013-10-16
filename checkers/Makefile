CC              := gcc
CFLAGS          := -Wall -ggdb
#CPPFLAGS        := -I./ -I/usr/X11R6/include/Xm -I/usr/X11R6/include -I/usr/include/openmotif
CPPFLAGS        := -I./
#LDFLAGS         := -L/usr/lib/X11R6 -lXm -lXaw -lXmu -lXt -lX11
#LDFLAGS         := -L/usr/X11R6/lib -L/usr/X11R6/LessTif/Motif1.2/lib -lXm -lXaw -lXmu -lXt -lX11 -lICE -lSM -pthread -L/usr/lib64/openmotif/
LDFLAGS         := -lX11 -lXm -lXaw -lXmu -lXt -lICE -lSM -pthread -L/home/reuben/Downloads/checkers/usr/

# Uncomment this next line if you'd like to compile the graphical version of the checkers server.
CFLAGS          += -DGRAPHICS

all: checkers computer
checkers: graphics.o checkers.o
	${CC} ${CPPFLAGS} ${CFLAGS} -o $@ $^ ${LDFLAGS}
computer: myprog.o
	${CC} ${CPPFLAGS} ${CFLAGS} -o $@ $^ ${LDFLAGS}


.PHONY: clean
clean:
	@-rm checkers computer *.o
