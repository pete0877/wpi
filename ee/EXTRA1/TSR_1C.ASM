; Peter Golaszewski
; Extra Credit for EE 3803 B98
; 11/4/98

; Program intercepts the 1c interrupt (clock interruct)
; and installs it's own procedure. The new interrupt
; procedure simply keeps displaying "Peter" in the
; top right corner of the screen

.286
dosseg
.model tiny
.stack   28h
.data
        id      dw (?)
        ad      dd (?)  ; address of the old interrupt
.code



new_procedure:
        cli
        ; We need to save all the registers
        push es
        push ds
        pusha
        pushf

jmp     over_data
        txt     db 'P',132,'e',132,'t',132,'e',132,'r',132
over_data:

        ; This part takes care of displaying the Peter message
        ; on the screen
        mov     ax,0b800h
        mov     es,ax
        xor     bx,bx
l0:     mov     cl,cs:[txt][bx]
        mov     es:[bx+150],cl
        inc     bx
        cmp     bx,10
        jne     l0
exit:
        ; Now we bring all the registers back
        popf
        popa
        pop ds
        pop es
        sti

        iret

install:
        ; First we find out where the old interrupt is
        ; sitting: ES:BX
        mov     ah,35h
        mov     al,1ch
        int     21h

        cli     ; interrupt mask flag - stop interrupts

        ; Put the address of the old interrupt
        ; into the memory for later:
        mov     word ptr [ad],bx
        mov     ax,es
        mov     word ptr ad[2],ax

        ; Here we install the new procedure
        ; using the dos function:
        mov     ax,seg new_procedure
        mov     ds,ax
        mov     dx,offset new_procedure
        mov     ah,25h
        mov     al,1ch
        int     21h

        ; This is the tricky part :
        ; We need to cause the installation program to stay in
        ; the memory after it installs the new procedure.

        ; Get the segment address to the ES
        mov     ax,@code
        sub     ax,10h
        mov     ds,ax
        mov     bx,2ch
        mov     ax,[bx]
        mov     es,ax
        mov     ah,49h
        int     21h

        ; Following will let us get access to the PSP
        mov     ah,62h
        int     21h
        mov     ax,@data
        mov     ds,ax
        mov     id,bx
        ; Now ID variable holds the segment of the PSP

        sti     ; interrupt mask flag - allow interrupts
        mov     dx,(offset ad)-1
        mov     cl,4
        shr     dx,cl
        add     dx,11h ; extra space for PSP

        ; Now set the return code to 3, and put the
        ; process to sleep:
        mov     ax,3103h
        int     21h

        ; We are done:
        mov     ah,4ch
        int     21h
end install
