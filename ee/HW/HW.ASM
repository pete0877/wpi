; Program:
;    HW Problem
; Authors:
;    Peter Golaszewski - peterg@wpi.edu
        .Model small
        .Stack 100h
;--------------------------------------------------------------------
; Program Data:
        .data
        DATA_ARRAY db 256 dup (0)
;--------------------------------------------------------------------
; Program Code:
        .code
        .386
program_start:

        ; Set up the source
        mov     ax,@data
        mov     ds,ax
        lea     si,DATA_ARRAY

        ; Set up destination
        mov     ax,0F000h
        mov     es,ax
        mov     di,0F02Ah

        ; User strings to move all the bytes:
        mov     cx,256
        rep     movsb

exit_program:
        mov     ax,4c00h  ; Call DOS interrupt to terminate the process
        int     21h       ;    and delocate all the memory.
;--------------------------------------------------------------------
; End of the program
end program_start

