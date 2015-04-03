; Peter M. Golaszewski
; CS 2135, section 4
; Homework #4 problem #4

; ACOVERED? tests whether all the variables in LEXP are in a the 
; ALIST

;-----------------------------------
;Help functions:
;-----------------------------------

;   This function returns list of variables seperated from thir
;  pairs. for example (seperate '((x . 3) (y . 4))) -> (x y)
(define seperate (lambda (alst) (
 cond 
  ((null? alst) '())
  (else (cons (caar alst) (seperate (cdr alst)) ))
)))

;   This function flattens the list lst, so that it ignores sublist's
(define Flatten (lambda (lst) (
  cond
  ((symbol? lst) (list lst))
  ((null? lst) '())
  (else (
         if (list? (car lst))      
         (append (Flatten (car lst)) (Flatten (cdr lst)))
         ;Else
         (cons (car lst) (Flatten (cdr lst)))
        )
  )
)))

;contains? of a {b,a,c} is true, contains? of a { } is false,
;contains? of a {b,a} is true
(define contains? (lambda (elt s) (
  cond
        ((null? s) #f)
        ((eq? elt (car s)) #t)
        (else (contains? elt (cdr s)))
)))

; subset? of {a,b} {c,b,a} is true, subset? of { } {a,b}   
; is true, subset? of {c,a,b} {a,c} is false
(define subset? (lambda (s1 s2) (
  cond
        ((null? s1) #t)
        ((contains? (car s1) s2) (subset? (cdr s1) s2))
        (else #f)
)))


;--------------------------------------------
; core function:
;-----------------------------------

(define ACOVERED? (Lambda (lxp alist)(
  subset? (flatten lxp)
          (append '(land lor lnot limplies lequivalent) (seperate alist))
)))                   
