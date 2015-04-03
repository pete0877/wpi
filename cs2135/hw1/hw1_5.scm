; Peter M. Golaszewski
; CS 2135, section 4
; Homework #1 problem #5
;  This pprogram takes a list lst and symbol s, and returns index (0,1 ..)
; of the first appearance of the symbol in the list or -1 if it doesn't
; occure

(define List-Index (lambda (s lst) (
	if (null? lst) -1
	; ELSE
	(
		if (eq? (car lst) s) 0
		; Else
		(
			; Search the rest of the list :
			if (eq? (list-index s (cdr lst)) -1) -1 
			(+ 1 (list-index s (cdr lst)))
		)
	)
)))
