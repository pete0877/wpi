;-------------------------------------------------------------------
; Peter Golaszewski
; CS4341 B98 Project 1
; 11/2/98
;-------------------------------------------------------------------
; initial input data:
(load "datafile.lsp")
;-------------------------------------------------------------------
; initialize some help variables:
(setf number_of_raters (caar input_data))
(setf number_of_items  (cadar input_data))
;-------------------------------------------------------------------
; nth_list_element - returns nth element of the list n=1..k
(defun nth_list_element (lst n) 
     (
      if (= n 1) 
          (car lst)
          (nth_list_element (cdr lst) (- n 1))
     )
)
;-------------------------------------------------------------------
; find_element_index - returns list index (1..k) of the given element
(defun find_element_index (lst element) 
  (
    if (eq lst '()) 0
    (
       if (eq (car lst) element) 1
          (+ 1 (find_element_index (cdr lst) element))  
    )
  )
)
;-------------------------------------------------------------------
; rating_get - returns rating of the item index given by the rater
(defun rating_get (data_list rater item_index) 
     (
      if (eq (caar data_list) rater)
        (nth_list_element (cadar data_list) item_index)
        (rating_get (cdr data_list) rater item_index)
     )
)
;-------------------------------------------------------------------
; rating - returns rating of the item ID given by the rater
(defun rating (rater itemID) 
     (
      rating_get (cadr input_data) 
                 rater 
                 (find_element_index (caddar input_data) itemID)
     )
)
;-------------------------------------------------------------------
; score_total - returns sum of all the scores given to the item (by index)
; in given list of scores
(defun score_total (item_index scores) 
     (
      if (eq scores '()) 0
      (+ (score_total item_index (cdr scores)) 
         (nth_list_element (cadar scores) item_index)
      )
     )
)
;-------------------------------------------------------------------
; featSim - returns difference between two scores, or NIL if one of the scores is 0
(defun featSim (score1 score2) 
     (
      if (or (eq score1 'NA) (eq score2 'NA)) NIL
         (abs (- score1 score2))        
     )
)
;-------------------------------------------------------------------
; total_common - returns number of pairs of valid scores from two lists
(defun total_common (scores1 scores2) 
     (
      if (or (eq (car scores1) 'NA) (eq (car scores2) 'NA)) 
        (
          if (eq scores1 '()) 0 (+ 0 (total_common (cdr scores1) (cdr scores2)))
        )        
        (
          if (eq scores2 '()) 0 (+ 1 (total_common (cdr scores1) (cdr scores2)))
        )                
     )
)
;-------------------------------------------------------------------
; listSum - returns sum of the list (for elements <> NIL)
(defun listSum (lst) 
     ( 
      if (eq lst '()) 0
        (
         if (eq (car lst) NIL) 
            (+ 0 (listSum (cdr lst)) )
            (+ (car lst) (listSum (cdr lst)) )
        )
     )
)
;-------------------------------------------------------------------
; listsDiff - returns list of differences between two lists
(defun listsDiff (scores1 scores2) 
     ( 
      if (eq scores1 '()) '()
        (
         cons (featSim (car scores1) (car scores2))
              (listsDiff (cdr scores1) (cdr scores2))
        )
     )
)
;-------------------------------------------------------------------
; totalSim_all - returns total number of differences between two lists
(defun totalSim_all (scores1 scores2) 
     ( listSum (listsDiff scores1 scores2) )
)
;-------------------------------------------------------------------
; totalSim - returns average difference between two score lists
(defun totalSim (scores1 scores2) 
   (if (= (total_common scores1 scores2) 0) NIL
     (/ 
        (totalSim_all scores1 scores2)
        (total_common scores1 scores2)        
     )
   )
)
;-------------------------------------------------------------------
; scores_except_rater - returns all ratings except the one of the rater 
(defun scores_except_rater (rater scores) 
  ( if (eq scores '()) '()
   (
     if (eq (caar scores) rater) (cdr scores)      
     (cons (car scores) (scores_except_rater rater (cdr scores)))
   )
  )
)
;-------------------------------------------------------------------
; scores_of_rater - returns scores list of the rater 
(defun scores_of_rater (rater scores) 
  ( if (eq scores '()) '()
   (
     if (eq (caar scores) rater) (cadar scores)      
       (scores_of_rater rater (cdr scores))
   )
  )
)
;-------------------------------------------------------------------
; scores_except_item - returns all item scores except the one of the item
; Format: ( (Item1,(1,3,4,5)), (Item2,(1,3,NA,5)), etc )
(defun scores_except_item (itemID) 
  ( scores_except_item_get itemID (caddar input_data))
)
;-------------------------------------------------------------------
; scores_except_item_get - help function to the  scores_except_item 
(defun scores_except_item_get (itemID rest_of_items) 
  (
   if (eq rest_of_items '()) '()
    (
       if (eq itemID (car rest_of_items)) 
              (scores_except_item_get itemID (cdr rest_of_items))    
              (
                cons (list (car rest_of_items) (scores_of_item (car rest_of_items)) )
                     (scores_except_item_get itemID (cdr rest_of_items))
              )
    )
  )
)
;-------------------------------------------------------------------
; scores_of_item - returns scores list of the item 
; Format: (1,4,2,3,NA)
(defun scores_of_item (itemID) 
  (
   scores_of_item_get 
       (find_element_index (caddar input_data) itemID)
       (cadr input_data)
  )
)
;-------------------------------------------------------------------
; scores_of_item_get - help function to scores_of_item
(defun scores_of_item_get (item_index all_scores) 
 (
  if (eq all_scores '()) '()
   (
     cons (nth_list_element (cadar all_scores) item_index)
     (scores_of_item_get item_index (cdr all_scores)) 
   )
 )
)
;-------------------------------------------------------------------
; kill_score - returns all scores except the one specified
(defun kill_score (ID scores) 
  ( if (eq scores '()) '()
   (
     if (eq (caar scores) ID) (cdr scores)      
       (cons (car scores) (kill_score ID (cdr scores)))
   )
  )
)
;-------------------------------------------------------------------
; compare_scores - returns array of similarity scores between the list 
; of scores and the given score (along with rater's ID):
; ((raterID,similarity), (raterID,similarity))
(defun compare_scores (scores rest_scores) 
  (
   if (eq rest_scores '()) '()
     (
      cons (
            list (caar rest_scores) (totalSim scores (cadar rest_scores))
           )
           (compare_scores scores (cdr rest_scores))
     )   
  )
)
;-------------------------------------------------------------------
; best_compare - returns rater ID of the best compare_scores output
; call it (compares_array,NIL,10) .. no one can score higher (worse) then 10
(defun best_compare (compares rater lowest) 
  (
   if (eq compares '()) rater
     (
       if (eq (cadar compares) NIL) (best_compare (cdr compares) rater lowest)
        (
          if (< (cadar compares) lowest) 
               (best_compare (cdr compares) (caar compares) (cadar compares))
               (best_compare (cdr compares) rater lowest)           
        )
     )   
  )
)
;-------------------------------------------------------------------
; nearestRaters - returns list of N closest raters
(defun nearestRaters (n rater) 
  (
    nearestRaters_get n 
        ( compare_scores 
                 (scores_of_rater rater       (cadr input_data) ) 
                 (scores_except_rater rater   (cadr input_data) )
        ) 
  )
)
;-------------------------------------------------------------------
; nearestraters_get - returns list of N closest raters using pre-formated data
(defun nearestraters_get (n compares) 
  (
    if (or (= n 0) (eq (best_compare compares NIL 10) NIL)) '()
    (
     cons (best_compare compares NIL 10) 
          (
            nearestraters_get (- n 1) 
                              (kill_score (best_compare compares NIL 10) compares)
          )
    )
  )
)
;-------------------------------------------------------------------
; nearestItems - returns list of N closest items
(defun nearestItems (n itemID) 
  (
    nearestItems_get n 
        ( compare_scores 
                 (scores_of_item itemID) 
                 (scores_except_item itemID)
        ) 
  )
)
;-------------------------------------------------------------------
; nearestItems_get - returns list of N closest items using pre-formated data
(defun nearestItems_get (n compares) 
  (
    if (or (= n 0) (eq (best_compare compares NIL 10) NIL)) '()
    (
     cons (best_compare compares NIL 10) 
          (
            nearestItems_get (- n 1) 
                             (kill_score (best_compare compares NIL 10) compares)
          )
    )
  )
)
;-------------------------------------------------------------------
; predictedRatingR_get - returns rating based on similar rater's rating and given similar raters
(defun predictedratingr_get (rater itemID other_raters) 
  (
   if (eq other_raters '()) 'no_prediction
    (
     if (eq (rating (car other_raters) itemID) 'NA)
       (predictedratingr_get rater itemID (cdr other_raters))
       (rating (car other_raters) itemID)
    )
  )
)
;-------------------------------------------------------------------
; predictedRatingI_get - returns rating based on similar item's rating and given the raters
(defun predictedRatingI_get (rater itemID other_items) 
  (
   if (eq other_items '()) 'no_prediction
    (
     if (eq (rating rater (car other_items)) 'NA)
       (predictedRatingI_get rater itemID (cdr other_items))
       (rating rater (car other_items))
    )
  )
)
;-------------------------------------------------------------------
; predictedRatingR - returns rating based on similar rater's rating
(defun predictedRatingR (rater itemID) 
  (predictedRatingR_get rater itemID (nearestraters number_of_raters rater))
)
;-------------------------------------------------------------------
; predictedRatingI - returns rating based on similar item's rating
(defun predictedRatingI (rater itemID) 
  (predictedRatingI_get rater itemID (nearestitems number_of_items itemID))
)
;-------------------------------------------------------------------
; END OF THE FILE
