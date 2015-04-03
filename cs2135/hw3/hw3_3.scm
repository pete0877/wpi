; Peter M. Golaszewski
; CS 2135, section 4
; Homework #3 problem #3

;  This is implementation of few basic procedures that can 
; be used to work with sets.

; empty-set? of { } is true, Kempty-set? of {a,b} is false :
(define empty-set? (lambda (s) (
	null? s
)))

; set? { } is true, set? of {a,b} is true, set? of {a,b,a} is false :
(define set? (lambda (s) (
  cond 
	((null? s) #t)
	((contains? (car s) (cdr s)) #f)
	(else (set? (cdr s)))
)))

; subset? of {a,b} {c,b,a} is true, subset? of { } {a,b} 
; is true, subset? of {c,a,b} {a,c} is false 
(define subset? (lambda (s1 s2) (
  cond 
	((null? s1) #t)
	((contains? (car s1) s2) (subset? (cdr s1) s2))
	(else #f)
)))

;set-equal? { } { } is true, set-equal? {a,b} {a,b} is true, 
;set-equal? {a} {a,b} is false 
(define set-equal? (lambda (s1 s2) (
  cond
	((and (null? (set-diff s1 (intersect s1 s2))) 
	      (null? (set-diff s2 (intersect s1 s2))) ) #t )
	(else #f)
)))

;contains? of a {b,a,c} is true, contains? of a { } is false, 
;contains? of a {b,a} is true 
(define contains? (lambda (elt s) (
  cond
	((null? s) #f)
 	((eq? elt (car s)) #t)
	(else (contains? elt (cdr s)))
)))

; Cardinality of { } is 0, cardinality of {a,b} is 2 
(define Cardinality (lambda (s) (
  cond
	((null? s) 0)
	(else (+ 1 (Cardinality (cdr s))))
)))

; addto of a { } is { a}, addto of a {c,b} is {a,c,b}, 
; addto of a {c,a,b} is {c,a,b} 
(define addto (lambda (elt s) (
  cond
	((contains? elt s) s)
	(else (cons elt s))
)))

; list->set of ( ) is { }, list->set of (a b) is {a,b}, 
; list->set of (a b a) is {a,b} 
(define list->set (lambda (s) (
  cond
	((null? s) '())
	((contains? (car s) (cdr s)) (list->set (cdr s)))
	(else (cons (car s) (list->set (cdr s))))
)))

; intersect of {c,a,b} {d,b,e} is {b}, 
; intersect of {c,a,b} {d,f,e} is { } 
(define intersect (lambda (s1 s2) (
  cond
	((null? s1) '())
	((contains? (car s1) s2) 
	   (cons (car s1) (intersect (cdr s1) s2))
	)
	(else (intersect (cdr s1) s2))
)))

; set-diff of {a,b,c} {e,c,d} is {a,b}, 
; set-diff of {b,c} { } is {b,c}, set-diff of { } {a} is { } 
(define set-diff (lambda (s1 s2) (
  cond
	((null? s1) '())
	((contains? (car s1) s2) 
	   (set-diff (cdr s1) s2)		   
	)
	(else (cons (car s1) (set-diff (cdr s1) s2)))
)))

; union of { } {a,b} is {a,b}, union of {a,c} {b,d} is {a,c,b,d}, 
; union of {a,b} {b,d} is {a,b,d} 
(define union (lambda (s1 s2) (
	append (set-diff s1 (intersect s1 s2)) s2
)))

; This is my help function that generates list of lists
; such : ((t e1) (t e2) ... (t en)) where t is a symbol
; and e1 .. en are elements of the list s.
(define maketerm (lambda (t s) (
	cond 	
	((null? s) '())
	(else (cons (list t (car s)) (maketerm t (cdr s)))  
        )
)))

; cartesian of { } {a,b} is { }, 
; cartesian of {a,b} {c,d,a,b} is 
; {(a c),(a d),(a a),(a b),(b c),(b d),(b a),(b b)} 
(define cartesian (lambda (s1 s2) (
	cond 	
	((null? s1) '())
	(else (append (maketerm (car s1) s2) (cartesian (cdr s1) s2)  ))
)))
