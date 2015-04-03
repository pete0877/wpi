; Peter M. Golaszewski
; CS 2135, section 4
; Homework #1 problem #6
;  This program Substitutes all 'old' elements in list 'lst' with
; 'new' elements using map statement

(define subst (lambda (new old lst) (
	if (null? lst) '()
	; ELSE
	(
	  map (lambda (expr) 
          (
           if (symbol? expr)
            (if (eq? old expr) new expr)
	   ; treat sublist
	    (subst new old expr)	
          )) lst
	)
)))
