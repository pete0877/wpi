; Peter M. Golaszewski
; CS 2135, section 4
; Homework #2 problem #5
;   This function takes a let expression (list) and returns the equivalent
; expression, also represented as a list: an application of a procedure
; created by lambda expr.

(define let->application (lambda (let) (
 append (list (list 'lambda (get-vars (cadr let)) (caddr let))) 
        (get-vals (cadr let))
)))


(define get-vars (lambda (lst) (
 if (null? lst) '()
 (
	cons (caar lst) (get-vars (cdr lst))
 )
)))

(define get-vals (lambda (lst) (
 if (null? lst) '()
 (
	cons (cadar lst) (get-vals (cdr lst))
 )
)))

