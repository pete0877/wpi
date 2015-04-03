set terminal x11
set data style linespoints
set title "Performance of Sorts in Terms of Comparisons"
set xlabel "n"
set ylabel "comparisons"
plot 'quick.dat', 'merge.dat', 'selection.dat', 'bubble.dat'
pause -1
