; Peter M. Golaszewski
; CS 2135, section 4
; Homework #4 problem #5

;EVALLEXP takes LEXP and ALIST which contains all the variables in
;the LEXP.
;(EVALLEXP LEXP ALIST) is true 
; - if LEXP is a variable reference and its value is 1, or 
; - if LEXP is an LAND-expression and both subexpressions yield true, or 
; - if LEXP is an LOR-expression and one of the subexpressions
;   yields true, or 
; - if LEXP is an LEQUIVALENT-expression and both subexpressions
;   yield the same true value, i.e. both true or both false, or 
; - if LEXP is an LIMPLIES-expression and the first subexpression is
;   false or the second subexpression is true, or 
; - if LEXP is a LNOT-expression and the subexpression yields false. 
;Otherwise EVALLEXP yields false. 

; Help functions:
;--------------------------------------------------

; LOOKUP of VAR and ALIST returns the value of the first
;  pair in ALIST whose car is eq? to VAR.

(define lookup (Lambda (var alist)(
 cond
  ((null? alist) #f)
  ((eq? (caar alist) var) (cdar alist))
  (else (lookup var (cdr alist)))
)))

; OR? .. logical OR on two boolean expressions
(define or? (Lambda (c1 c2)(
 cond
  ((eq? c1 #t) #t)
  ((eq? c2 #t) #t)
  (else #f)
)))

;--------------------------------------------------

(define EVALLEXP (Lambda (LEXP ALIST)(
cond
; Variable evaluation :   
 ((symbol? LEXP)
   (if (eq? (lookup LEXP ALIST) 1) #t #f)
 )
; LAND evaluation:
 ((eq? (car lexp) 'land)
   (and (EVALLEXP (cadr lexp) alist) 
        (EVALLEXP (caddr lexp) alist) )
 )
; LOR evaluation:
 ((eq? (car lexp) 'lor)
   (or (EVALLEXP (cadr lexp) alist) 
       (EVALLEXP (caddr lexp) alist) )
 )
; LEQUIVALENT evaluation:
 ((eq? (car lexp) 'lequivalent)
   (eq? (EVALLEXP (cadr lexp) alist) 
        (EVALLEXP (caddr lexp) alist) )
 )
; LIMPLIES evaluation:
 ((eq? (car lexp) 'limplies)
   (or? (eq? (EVALLEXP (cadr lexp) alist) #f ) 
        (EVALLEXP (caddr lexp) alist) )
 )
; LNOT evaluation:
 ((eq? (car lexp) 'lnot)
   (eq? (EVALLEXP (cadr lexp) alist) #f )         
 )
 ( else #f )
)))

;--------------------------------------------------  

