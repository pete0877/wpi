; Peter M. Golaszewski
; CS 2135, section 4
; Homework #2 problem #6
;   This function takes two polunomials and multiplies them together

(define POLY-MULT (lambda (p1 p2) (
        if (null? p1) '()
	( 
	  FILTER0 ( POLY-ADD (POLY-MULT-TERM (car p1) p2)
	  	             (POLY-MULT (cdr p1) p2)
		  )
	)
)))

(define POLY-ADD (lambda (pp1 pp2) (
  if (or (null? pp1) (null? pp2)) 
  (append pp1 pp2)
  (
    ;Adds two polies
    ; Find greater power of the leading terms of the pp1 and pp2,
    ; and add it to the rest list 
    if (< (cadar pp1) (cadar pp2))  ;get the pp2[1]
    (cons (car pp2) (POLY-ADD pp1 (cdr pp2)))
    ;Else
    (
	if (> (cadar pp1) (cadar pp2))  ;get the pp1[1]
	(cons (car pp1) (POLY-ADD (cdr pp1) pp2))
	( 
	   ;  the terms have same powers, so we can add them:
 	   cons (TERM-ADD-TERM (car pp1) (car pp2)) 
	        (POLY-ADD (cdr pp1) (cdr pp2))
	)
    )
  )
)))

(define POLY-MULT-TERM (lambda (t pp) (
	if (null? pp) '()
	; ELSE
	(
		cons (TERM-MULT-TERM t (car pp))
		     (POLY-MULT-TERM t (cdr pp))
	)
)))

(define TERM-MULT-TERM (lambda (t1 t2) (
	list (* (car t1) (car t2)) (+ (cadr t1) (cadr t2))
)))

(define TERM-ADD-TERM (lambda (tt1 tt2) (
	list (+ (car tt1) (car tt2)) (cadr tt1)
)))

(define FILTER0 (lambda (poly) (
	if (null? poly) '()
	; Else
	(
		if (zero? (caar poly))
		(FILTER0 (cdr poly))
		; Term is ok:
		(cons (car poly) (FILTER0 (cdr poly)))
	)
)))




