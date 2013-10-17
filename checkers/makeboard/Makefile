CC              := gcc
#CFLAGS          := -Wall -ggdb -L/usr/X11R6/lib -L /usr/X11R6/LessTif/Motif1.2/lib -lXm -lXaw -lXmu -lXt -lX11 -lICE -lSM -pthread -L/usr/lib64/openmotif/ -L/home/faculty/tim/motif/usr/lib64
CFLAGS          := -Wall -ggdb 
LDFLAGS         := -lX11 -lXm -lXaw -lXmu -lXt -lICE -lSM -pthread -L/home/reuben/dev/ai/checkers/usr/
CPPFLAGS        := -I./
#CPPFLAGS        := -I./ -I/usr/X11R6/include/Xm -I/usr/X11R6/include -I/usr/include/openmotif -I/home/faculty/tim/motif/usr/include
#LDFLAGS         := -L/usr/lib/X11R6 -lXm -lXaw -lXmu -lXt -lX11
#LDFLAGS         := -L/usr/X11R6/lib -L /usr/X11R6/LessTif/Motif1.2/lib -lXm -lXaw -lXmu -lXt -lX11 -lICE -lSM -pthread -L/usr/lib64/openmotif/ -L/home/faculty/tim/motif/usr/lib64

# Uncomment this next line if you'd like to compile the graphical version of the checkers server.
CFLAGS          += -DGRAPHICS

all: makeboard 
makeboard: graphics.o makeboard.c

	${CC} ${CPPFLAGS} ${CFLAGS} -o $@ $^ ${LDFLAGS}


.PHONY: clean
clean:	
	@-rm makeboard *.o
