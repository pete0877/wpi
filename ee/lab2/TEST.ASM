        .Model small
        .Stack 100h
        .data

        IO_PORT          equ 0320h ; port we send to
        DELAY            equ 10 ; display delay in seconds
        STOP             equ 255

        test_data        db 1,0,2,0,3,0,4,0,5,0,6,0,7,0   ; displayed data (includes resets - 0's)
                         db 8,0,9,0,11,0,12,0,13,0,14,0,15,0,STOP
        display_data     db '1',32,'2',32,'3',32,'4',32,'5',32,'6',32,'7',32
                         db '!',32,'@',32,'#',32,'$',32,'%',32,'^',32,'&',32, 32
        current          dw 0

        .code
        .386

program_start:

        mov     ax,@data
        mov     ds,ax

main_program_loop:

        mov     ah,1
        int     16h

        jnz     quit

        call    display_next_data

        jmp     main_program_loop

quit:
        mov     ah,0
        int     16h

        mov     ax,4c00h
        int     21h

;--------------------------------------------------------------------
; Program Procedures:
;--------------------------------------------------------------------
display_next_data:
        pusha

        ; get the values of the upcoming value to send and the
        ; character for the display
        mov     bx,current
        mov     al,test_data[bx]
        mov     dl,display_data[bx]
        inc     bx
        mov     ah,test_data[bx]
        cmp     ah,STOP
        jne     set_position
        mov     bx,0

set_position:
        mov     current,bx

        mov     ah,2 ; display the character on the screen
        int     21h

        ; send the value to the port
        mov     dx,IO_PORT
        mov     cl,4
        shl     al,cl ; adjust the value to fit the format
        out     dx,al

        ; cause some delay
        mov     dx,DELAY
        call    time_delay

        popa
        ret
;--------------------------------------------------------------------
time_delay:
; waits 1/55 * DX seconds
        ; save needed values
        pusha
        mov   bx,dx

        ; get the current timer value into di
        mov   ah,0
        int   1ah
        mov   di,dx
td:
        ; get the current timer value into dx
        mov   ah,0
        int   1ah
        sub   dx,di
        cmp   dx,bx
        jb    td

        popa
        ret
;--------------------------------------------------------------------
; End of the program
end program_start

