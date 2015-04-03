; Peter M. Golaszewski
; CS 2135, section 4
; Homework #2 problem #2
;   This function counts occurences of the s in lst, and its sublists.

(define Count-Occurrences (lambda (s lst) (
  if (null? lst) 0
  ; ELSE
  (
	if (eq? (car lst) s) 
	   (+ 1 (Count-Occurrences s (cdr lst)))  ; Found one 
	;ELSE
	(if (list? (car lst)) 
	(+ (Count-Occurrences s (car lst)) 
           (Count-Occurrences s (cdr lst))
        )
	; NOT A LIST :
           (Count-Occurrences s (cdr lst))

	)
  )
)))

