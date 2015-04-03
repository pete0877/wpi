; Peter M. Golaszewski
; CS 2135, section 4
; Homework #1 problem #2
;  This program takes a a symbol and a list as the arguments
; and returns number of top level elements in the list, that 
; are equal to the passed symbol. It returns 0 in case the 
; symbol does not ocure in the list. This function can be also 
; applied to counting occurences of numbers in a list.
; This function is self-recursive.
                                                          
(define Occure (lambda (smb lst) (
  if (null? lst) 0 
  ; ELSE :
  (
	if (eq? smb (car lst)) 
	(
		+ 1 (Occure smb (cdr lst))
	)
	; ELSE
	(
		Occure smb (cdr lst)
	)
  )
)))

