;-------------------------------------------------------------------
; Peter Golaszewski
; Tim Obrien
; Adnan Mesalic
; Dave ?
; Team: "xquake"
; CS4341 B98 Project 3
; 12/12/98
;-------------------------------------------------------------------

(load "proj2.lsp")

; predictedRatingR - now returns 'Like 'NotLike based on similar rater's rating
(defun predictedRatingR (rater itemID) 
  (if (> (predictedRatingR_get rater itemID (nearestraters number_of_raters rater)) 3)
     'Like
     'NotLike)
)
;-------------------------------------------------------------------

; predictedRatingI - now returns 'Like 'NotLike based on similar item's rating
(defun predictedRatingI (rater itemID) 
  (if (> (predictedRatingI_get rater itemID (nearestitems number_of_items itemID)) 3)
     'Like
     'NotLike)
)
;-------------------------------------------------------------------

; fake predictRatingIT
(defun predictRatingIT (rater itemID)
   'Like)
;-------------------------------------------------------------------

; fake predictRagingIC
(defun predictRatingIT (rater itemID)
   'NotLike)
;-------------------------------------------------------------------


; GetWeightF1 - returns correct number of predictions (weight) using predictedRatingR
(defun GetWeightF1 (rirlist)
   (let ((exList rirlist)
         (correctPredictions 0)
         (curExample NIL))
      (loop
        (when (endp exList) (return correctPredictions))
        (setf curExample (first exList))
        (when (eq (predictedRatingR (first curExample) (second curExample)) (third curExample))
           (setf correctPredictions (+ correctPredictions 1)))
        (setf exList (rest exList)))))
;-------------------------------------------------------------------

; GetWeightF2 - returns correct number of predictions (weight) using predictedRatingI
(defun GetWeightF2 (rirlist)
   (let ((exList rirlist)
         (correctPredictions 0)
         (curExample NIL))
      (loop
        (when (endp exList) (return correctPredictions))
        (setf curExample (first exList))
        (when (eq (predictedRatingI (first curExample) (second curExample)) (third curExample))
           (setf correctPredictions (+ correctPredictions 1)))
        (setf exList (rest exList)))))
;-------------------------------------------------------------------

; GetWeightF3 - returns correct number of predictions (weight) using predictedRatingT
(defun GetWeightF3 (rirlist)
   (let ((exList rirlist)
         (correctPredictions 0)
         (curExample NIL))
      (loop
        (when (endp exList) (return correctPredictions))
        (setf curExample (first exList))
        (when (eq (predictedRatingT (first curExample) (second curExample)) (third curExample))
           (setf correctPredictions (+ correctPredictions 1)))
        (setf exList (rest exList)))))
;-------------------------------------------------------------------

; GetWeightF4 - returns correct number of predictions (weight) using predictedRatingIC
(defun GetWeightF4 (rirlist)
   (let ((exList rirlist)
         (correctPredictions 0)
         (curExample NIL))
      (loop
        (when (endp exList) (return correctPredictions))
        (setf curExample (first exList))
        (when (eq (predictRatingIC (first curExample) (second curExample)) (third curExample))
           (setf correctPredictions (+ correctPredictions 1)))
        (setf exList (rest exList)))))
;-------------------------------------------------------------------

; GetWeightF5 - returns correct number of predictions (weight) using predictedRatingIT
(defun GetWeightF5 (rirlist)
   (let ((exList rirlist)
         (correctPredictions 0)
         (curExample NIL))
      (loop
        (when (endp exList) (return correctPredictions))
        (setf curExample (first exList))
        (when (eq (predictRatingIT (first curExample) (second curExample)) (third curExample))
           (setf correctPredictions (+ correctPredictions 1)))
        (setf exList (rest exList)))))
;-------------------------------------------------------------------