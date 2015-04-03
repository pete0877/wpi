# script of commands to average the 4th field
BEGIN{sum=0.0} {sum = sum + $4} END{print sum/NR}
