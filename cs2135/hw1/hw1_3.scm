; Peter M. Golaszewski
; CS 2135, section 4
; Homework #1 problem #3
;  This program takes a list as the argument,
; and returns same list bat with all the numbers removed
; This function is self-recursive.
                                                          
(define Out-Nums (lambda (lst) (
  if (null? lst) '() 
  ; ELSE :
  (
	if (number? (car lst))
	; FOUND NUMBER :
	(Out-Nums (cdr lst))
	; ELSE
	(
	  if (list? (car lst)) 
	  ; Go inside the list
	  (cons (out-nums (car lst)) (Out-Nums (cdr lst)))
	  ; leave as it is
	  (cons (car lst) (Out-Nums (cdr lst)))
        )
  )
)))

