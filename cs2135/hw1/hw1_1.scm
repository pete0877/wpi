; Peter M. Golaszewski
; CS 2135, section 4
; Homework #1 problem #1
;  This program takes a list as a argument, and returns
; number of top level elements in the list, or it returns 0
; in case list is empty. This function is self-recursive. 

(define COUNT (lambda (lst) (
  if (null? lst) 0 
  ; ELSE
  (+ 1 (COUNT (cdr lst)))
)))

