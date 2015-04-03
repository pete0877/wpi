set terminal x11
set data style linespoints
set title "Performance of Sorts in Terms of Time"
set xlabel "n"
set ylabel "seconds"
plot 'quick.tdat', 'merge.tdat', 'selection.tdat', 'bubble.tdat'
pause -1
