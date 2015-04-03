; Peter M. Golaszewski
; CS 2135, section 4
; Homework #2 problem #3
;   This function flattens the list lst, so that it ignores sublist's 

(define Flatten (lambda (lst) (
  if (null? lst) '()
  ; ELSE
  (
	if (list? (car lst))
	(append (Flatten (car lst)) (Flatten (cdr lst)))
	;Else
	(cons (car lst) (Flatten (cdr lst)))
  )
)))

