; Peter M. Golaszewski
; CS 2135, section 4
; Homework #4 problem #3

; LOOKUP of VAR and ALIST returns the value of the first 
;  pair in ALIST whose car is eq? to VAR. 

(define lookup (Lambda (var alist)(
 cond 
  ((null? alist) #f)
  ((eq? (caar alist) var) (cdar alist))
  (else (lookup var (cdr alist)))
)))
