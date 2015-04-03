; Program: Test driver for the keypad
; Authors: Peter Golaszewski and Mike Pachos
; Course : EE3803
; Project: Lab3
;-------------------------------------------------------------------
        .model small
        .stack 100h
        .data

        TEST_PORT        EQU 0320H ; PORT WE SEND THE 5 BITS TO
        RESULTS_PORT     EQU 0321H ; PORT WE RECEIVE THE RESULTS FROM

        PORTDELAY        EQU 1     ; NUMBER OF 1/55 SEC WE WANT TO
                                   ; WAIT TILL WE DECIDE TO THAT THE
                                   ; KEY IS PRESSED
        keybits          db 01111b, 10111b, 11011b, 11101b, 11110b
        sentcount        dw 0

        KEYMAP           DB "F3 $","F  $","B  $","7  $","3  $"
             FUN2        DB "F2 $","E  $","A  $","6  $","2  $"
             FUN1        DB "F1 $","D  $","9  $","5  $","1  $"
             FUN0        DB "F0 $","C  $","8  $","4  $","0  $"
                         DB "SPS$","ENT$",">  $","DEL$","<  $"
      ; used by the effect1:
      SLOW = 02222H
      SPEED = 6
      fname        db "mikepete.pcx",0
      file_err_mgs db "error occured while accessing the image file !!! $"
      full_pal     db 768 dup (?)
      cur_pal      db 768 dup (?)
      img          db 60500 dup (?)
      ; used by effect2
      f   db 'font.dat',0
      f2  db 'sin4.dat',0
      sin     db 200 dup (?)
      tekst   db '       THIS PROGRAM WAS MADE BY MIKE PACHOS AND PETER GOLASZEWSKI ......... @' ;*** tekst
      wpl     dw 0
      wll     dw 0
      ps  dw 0
      czy db 0
;-------------------------------------------------------------------
        .code
        .386
program_start:
        mov     ax,@data
        mov     ds,ax
reset_counter:
        mov     bx,0
        mov     sentcount,bx
main_program_loop:
        ; check if a key was pressed.
        ; if yes, then quit the program
        mov     ah,1
        int     16h
        jnz     quit
        ; get the next check bit sequece to be sent to
        ; the port
        mov     bx,sentcount
        mov     dx,320h
        mov     al,keybits[bx]

        ; send the sequence
        out     dx,al

        mov     dx,portdelay
        call    time_delay

        ; see if a key is pressed:
        mov     dx,321h
        in      al,dx
        ;mov     al,01111b

        cmp     al,11111b
        je      nothing_there

        cmp     al,0
        je      nothing_there

        ; key was pressed. we want to check it again
        ; in a moment, to make sure it's really there.

        mov     ah,al

        mov     dx,portdelay
        call    time_delay

        ; check again
        mov     dx,321h
        in      al,dx
        ;mov     al,01111b

        ; see if the value is still there:
        cmp     al,ah
        jne     nothing_there

        ; yes, let's find out what it was:
        ; bx holds 0 .. 4, which indicates the row of the
        ; keymap. al holds what we got back from the port.
        ; we should first find out what is the value that
        ; we got back:

        mov     cx,bx ; save it for later
        mov     bx,0
lookforal:
        mov     ah,keybits[bx]
        cmp     al,ah
        je      found_it_at_bx
        inc     bx
        cmp     bx,5
        je      quit ; something went wrong .. we got junk from the port!
        jmp     lookforal
found_it_at_bx:

        ; now we just need to get to the right string, so that
        ; we can display the string on the screen:

        mov     al,cl
        mov     ah,0
        mov     cl,5
        mul     cl
        add     al,bl
        mov     cl,2
        shl     al,cl

        lea     dx,keymap
        add     dx,ax

        cmp     dx,offset fun0
        jne     no_effect1
        ; display effect 1
        call    effect1
no_effect1:
        cmp     dx,offset fun1
        jne     no_effect2
        ; display effect 2
        call    effect2
no_effect2:
        ; display the string under ds:dx
        mov     ah,9
        int     21h

nothing_there:
        ; no key was pressed, we try the next sequence
        mov     bx,sentcount
        cmp     bx,4
        je      reset_counter

        inc     bx
        mov     sentcount,bx

        jmp     main_program_loop
quit:
        mov     ah,0
        int     16h

        mov     ax,4c00h
        int     21h

;--------------------------------------------------------------------
time_delay:
; waits 1/55 * dx seconds
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
effect1:
      pusha
      push es

      mov     ax,0013h
      int     10h

      lea     dx,fname   ; open file
      mov     ax,3d00h
      int     21h
      jnc     no_error0
      jmp     file_error
no_error0:
      mov     bx,ax

      mov     ax,4202h   ; move to the end of file
      mov     cx,0
      mov     dx,0
      int     21h
      jnc     no_error1
      jmp     file_error
no_error1:
      mov     di,ax      ; size of the file

      mov     ax,4200h   ; move to the begining + 128
      mov     cx,0
      mov     dx,128
      int     21h
      jnc     no_error2
      jmp     file_error
no_error2:

      mov     ax,3f00h ; read in the image data
      mov     cx,di
      sub     cx,768+128
      lea     dx,img
      int     21h
      jnc     no_error3
      jmp     file_error
no_error3:

      mov     ax,3f00h ; read in the pallete
      mov     cx,768
      lea     dx,full_pal
      int     21h
      jnc     no_error4
      jmp     file_error
no_error4:

      mov     ax,3e00h   ; close file
      int     21h
      jnc     no_error5
      jmp     file_error
no_error5:

      call    clear_pallete
      call    set_pallete

      ; decode the image data:
      mov     si,di
      ; si = size of the file - 128 - 768 + offset of img
      add     si,(offset img) - 128 - 768
      lea     bx,img
      mov     di,0
      mov     ax,0a000h
      mov     es,ax
main_decode_loop:
      mov     al,[bx]
      mov     ah,al
      and     al,192
      cmp     al,192
      jne     l00
        inc     bx
        mov     cl,[bx]
        mov     ch,ah
        and     ch,63
        mov     dl,0
l02:
        inc     dl
        mov     es:[di],cl
        inc     di
        cmp     dl,ch
        jne     l02
      jmp     l01
l00:
        mov     es:[di],ah
        inc     di
l01:
      inc     bx
      cmp     bx,si
      jne     main_decode_loop

      call    light_up
      call    wait_for_esc
      call    dark_down

exit:
      mov     ax,3
      int     10h
      jmp     theendef1

file_error:
      mov     ax,3
      int     10h
      lea     dx,file_err_mgs
      mov     ah,9
      int     21h
      call    wait_for_esc
      jmp     exit

theendef1:

        mov     ah,0
        int     16h

        pop es
        popa
        ret
;--------------------------------------------------------------------
; Help functions used by effect1:
;-------------------------------------------------------------------
light_up proc
      mov     di,0
ll06:
      lea     bx,cur_pal
      lea     si,full_pal
      mov     al,1
      inc     di
      cmp     di,speed
      jne     no_sync
       xor     di,di
       call    sync_wait
      no_sync:
      mov     cx,768
ll10:
      mov     ah,[bx]
      mov     dh,[si]
      cmp     ah,dh
      je      no_inc
      inc     ah
      mov     [bx],ah
      mov     al,0
no_inc:
      inc     bx
      inc     si
      loop    ll10
      call    set_pallete
      cmp     al,0
      je      ll06
      ret
endp
;-------------------------------------------------------------------
dark_down proc
      mov     di,0
l06:
      lea     bx,cur_pal
      mov     al,1
      inc     di
      cmp     di,speed
      jne     no_sync2
       xor     di,di
       call    sync_wait
      no_sync2:
      mov     cx,768
l10:
      mov     ah,[bx]
      cmp     ah,0
      jna     no_dec
      dec     ah
      mov     [bx],ah
      mov     al,0
no_dec:
      inc     bx
      loop    l10
      call    set_pallete
      cmp     al,0
      je      l06
      ret
endp
;-------------------------------------------------------------------
time_wait proc
      push   ax
      push   cx
      mov    ax,1
l08:
      mov    cx,slow
l09:  loop   l09
      dec    ax
      cmp    ax,0
      jne    l08
      pop    cx
      pop    ax
      ret
endp
;-------------------------------------------------------------------
wait_for_esc:
      in      al,60h
      cmp     al,1
      jne     wait_for_esc
      ret
endp
;-------------------------------------------------------------------
clear_pallete proc
      lea     bx,cur_pal
      mov     al,0
      mov     cx,768
l05:  mov     [bx],al
      add     bx,1
      loop    l05
      ret
endp
;-------------------------------------------------------------------
set_pallete proc
      push    di
      pushad

      mov     bx,0
      mov     dx,3c8h
      lea     di,cur_pal
l04:
      mov     al,bl
      out     dx,al
      inc     dx
      mov     al,ds:[di]
      shr     al,1
      shr     al,1
      out     dx,al
      mov     al,ds:[di+1]
      shr     al,1
      shr     al,1
      out     dx,al
      mov     al,ds:[di+2]
      shr     al,1
      shr     al,1
      out     dx,al
      dec     dx
      add     di,3
      inc     bl
      cmp     bl,255
      jb      l04

      popad
      pop     di
      ret
endp
;-------------------------------------------------------------------
sync_wait proc
      pusha
      mov  dx,03dah
z:    in   al,dx
      mov  ah,8
      and  al,ah
      cmp  al,0
      je   z

z2:    in   al,dx
      mov  ah,8
      and  al,ah
      cmp  al,0
      jne   z2

      popa
      ret
endp
;--------------------------------------------------------------------
effect2:
      pusha
      push es

      xor ax,ax
      mov wpl,ax
      mov wll,ax
      mov ps,ax
      mov czy,al

        mov     ax,0a000h
        mov     es,ax

        mov     dx,offset f
        mov     al,0
        mov     ah,3dh
        int     21h
        mov     bx,ax
        mov     ah,3fh
        mov     cx,60416
        mov     dx,offset img
        int     21h
        mov     ah,3eh
        int     21h
        mov     dx,offset f2
        xor     al,al
        mov     ah,3dh
        int     21h
        mov     bx,ax
        mov     ah,3fh
        mov     cx,200
        mov     dx,offset sin
        int     21h
        mov     ah,3eh
        int     21h
        mov     ax,13h
        int     10h
        mov cx,1
        mov dx,3c8h
        mov     bx,0
l000:
        mov al,cl
        out dx,al
        inc dx
        mov al,bl
        out dx,al
        mov al,0
        out dx,al
        mov al,bl
        out dx,al
        dec dx
        add bx,5
        inc cx
        cmp cx,256
        jb  l000
petla:  call    scroll
push dx
        mov     dx,03dah
@@33:
        in      al,dx
        test    al,08h
        jz     @@33
@@44:
        in      al,dx
        test    al,08h
        jnz      @@44
pop dx

                in  al,60h
                cmp al,1
                jne l004
                mov     ah,0
                int     16h
        koniec: sti
                mov ax,0003h
                int 10h
                jmp quiteffect2

        l004:   mov     cx,4
        xtimes: mov ax,ps
                cmp ax,199
        jz  zero
                inc     ax
                mov ps,ax
        jmp l005
        zero:   xor     ax,ax
                mov ps,ax
        l005:   loop    xtimes
                mov     bx,wpl
                cmp     tekst[bx+7],64
jnz petla    ;*** gdy jeszcze nie wszystkie litery wyswietlilismy i koniec dema.
jmp koniec

quiteffect2:
      mov     ax,3
      int     10h

      pop  es
      popa
      ret
;--------------------------------------------------------------------
; Help functions used by effect2:
scroll:
        mov bx,wpl
        mov bp,wll
        xor     cx,cx
p1:     mov     dl,tekst[bx]
                sub dl,32
                xor     dh,dh
                mov     ax,1024
                mul     dx
                mov     di,ax
                lea si,img[di]
                mov     dx,bp
                mov     ax,32
                mul dx
                add     si,ax
                mov     ax,cx
                mov     dx,320
                mul dx
                mov di,cx
                add di,ps
                cmp     di,199
                jb  ok
                sub     di,199
        ok: xor     dh,dh
                mov     dl,sin[di]
                add ax,dx
                mov di,ax
                push    cx
                mov     cx,16
                rep movsw
                pop     cx
                cmp bp,31
        jnz incr
                xor     bp,bp
                inc     bx
        incr:   inc     bp
                inc     cx
                cmp cx,200
jnz     p1
        cmp     wll,31
jnz     incr2
        xor     ax,ax
        mov wll,ax
        inc wpl
incr2:  inc wll
        ret
endp
;--------------------------------------------------------------------
; end of the program
end program_start

