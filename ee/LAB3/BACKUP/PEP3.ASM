; Program:
;    Piano Editor / Player v3.0
; Authors:
;    Peter Golaszewski - peterg@wpi.edu
;    Mike Pachos       - mpachos@wpi.edu
; Assigment:
;    WPI EE3803 B98, Lab 3
; Date:
;    12-7-98
;--------------------------------------------------------------------
; This program is the same as the version 1.0 (lab 0) of the
; program, except that it takes communicates with the
; Key pad.
;--------------------------------------------------------------------
; Compilation parameters:
        .Model small
        .Stack 100h
;--------------------------------------------------------------------
; Program Data:
        .data

        ; Define constants:
        TRUE             equ 1    ; Standard language help constants
        FALSE            equ 0
        MAX_SCREEN_SIZE  equ 25 * 80 ; Screen parameters
        MAX_LINES_NUMBER equ 25
        MSG_X_POSITION   equ 5    ; Fixed message position on the screen
        MSG_Y_POSITION   equ 21
        MODE_READY       equ 0    ; Modes constats
        MODE_RECORD      equ 1
        FINGER_CHARACTER equ 219  ; ASCCI code
        SPACE_CHARACTER  equ 32   ; ASCCI code
        MAX_SONG_LEN     equ 1000 ; Number of available tones per song
        BAR_TOP          equ 120
        BAR_SPEED        equ 4

        TEST_PORT        EQU 0320H ; PORT WE SEND THE 5 BITS TO
        RESULTS_PORT     EQU 0321H ; PORT WE RECEIVE THE RESULTS FROM

        PORTDELAY        EQU 1     ; NUMBER OF 1/55 SEC WE WANT TO
                                   ; WAIT TILL WE DECIDE TO THAT THE
                                   ; KEY IS PRESSED

        ; Define variables
        screen        db MAX_SCREEN_SIZE dup (?), "$"
        screed_file   db "screen.dat",0
        song_file     db 13,?,13 dup (?),0,0,0
        line_feed     db 13,10,"$"
        msg_ready     db "Status: ready ... ", "$"
        msg_recording db "Status: now recording ... ", "$"
        msg_playing   db "Status: now playing ... ", "$"
        msg_saved     db "Status: song saved, ready ... ", "$"
        msg_loaded    db "Status: song loaded, ready ... ", "$"
        msg_fnsave    db "Enter the filename to save to: ", "$"
        msg_fnload    db "Enter the filename to load from: ", "$"
        msg_error     db "Error found! Try again ... ", "$"
        msg_clear     db "                                                   ", "$"
        current_song  dw 2 * MAX_SONG_LEN dup(0)  ; (Format: tone1, time1, tone2, time2, ...)
        song_offset   dw 0
        song_size     dw 0
        finger_x      db 0
        finger_y      db 0
        program_mode  db MODE_READY
        last_tone     dw 0
        last_timer    dw 0
        fx_mode       db FALSE
        fx_bars       db 14 dup (?)

        keybits          db 10111b, 11011b, 11101b, 11110b
        sentcount        dw 0

;--------------------------------------------------------------------
; Program Code:
        .code
        .386

program_start:

        ; Set up the registers
        mov     ax,@data
        mov     ds,ax

        ; Call initialization procedures
        call    load_data_files
        call    show_main_screen

        ; Show the initial message
        lea     di,msg_ready
        call    show_the_message

        ; set the kayboard delays
        call    quick_key_delay

        mov     bx,0
        mov     sentcount,bx

main_program_loop:

        ; Check if there is a character on the keyboard:
        mov     ah,1
        int     16h
        jnz     keyboard_character
        ; no, there isn't a keyboard character waiting,
        ; let's check the pad:
        call    check_key_pad
        ; now, if no key was pressed at the keypad,
        ; al will be 0. Otherwise, it will store the
        ; ASCII code of the key pressed.
        cmp     al,0
        je      main_program_loop

        jmp     process_character

keyboard_character:
        ; Read the character from the keyboard
        mov     ah,0
        int     16h
process_character:
        ; Convert to upper case:
        cmp     al,'a'-1
        jna     not_lower_case
        cmp     al,'z'+1
        jnb     not_lower_case
        sub     al,32

not_lower_case:
        ; Check for the key pressed:

        cmp     al,'R'  ; ... start recording
        jne     not_R
        call    start_recording
not_R:
        cmp     al,'E'  ; ... stop recording
        jne     not_E
        call    stop_recording
not_E:
        cmp     al,'P'  ; ... start playing
        jne     not_P
        call    start_playing
not_P:
        cmp     al,'F'  ; ... start playing with FX
        jne     not_F
        call    start_playing_FX
not_F:
        cmp     al,'S'  ; ... save the song
        jne     not_S
        call    slow_key_delay
        call    save_current_song
        call    quick_key_delay
not_S:
        cmp     al,'L'  ; ... load a song
        jne     not_L
        call    slow_key_delay
        call    load_a_song
        call    quick_key_delay
not_L:
        cmp     ah,1    ; keyboard code for Esc key
        jna     not_1_7
        cmp     ah,9    ; keyboard code for 8 and * keys
        jnb     not_1_7

        call    play_tone

        ; set the kayboard delays back
not_1_7:
        cmp     al,'Q'  ; ... quit the program
        jne     main_program_loop
        call    slow_key_delay
exit_program:
        mov     ax,4c00h  ; Call DOS interrupt to terminate the process
        int     21h       ;    and delocate all the memory.
;--------------------------------------------------------------------
; Program Procedures:
;--------------------------------------------------------------------
check_key_pad:

        ; get the next check bit sequece to be sent to
        ; the port
        mov     bx,sentcount
        mov     dx,320h
        mov     al,keybits[bx]

        ; send the sequence
        out     dx,al

        mov     dx,portdelay
        call    time_dalay_4FX

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
        call    time_dalay_4FX

        ; check again
        mov     dx,321h
        in      al,dx
        ;mov     al,01111b

        ; see if the value is still there:
        cmp     al,ah
        jne     nothing_there

        ; yes, let's find out what it was:
        mov     bx,sentcount
        mov     ah,keybits[bx]
        ; AH now stores the value that we sent out
        ; And AL stores the value that we got back

        mov     bl,0 ; This will store final ASCII result
;----------------------------
        cmp     ah,10111b
        jne     nextsq1

        cmp     al,01111b
        jne     nextkey_0_0
        mov     bl,'1'
nextkey_0_0:
        cmp     al,10111b
        jne     nextkey_0_1
        mov     bl,'5'
nextkey_0_1:
        cmp     al,11011b
        jne     nextkey_0_2
        mov     bl,'@'
nextkey_0_2:
        cmp     al,11101b
        jne     nextkey_0_3
        mov     bl,'^'
nextkey_0_3:
;----------------------------
nextsq1:
        cmp     ah,11011b
        jne     nextsq2

        cmp     al,01111b
        jne     nextkey_1_0
        mov     bl,'2'
nextkey_1_0:
        cmp     al,10111b
        jne     nextkey_1_1
        mov     bl,'6'
nextkey_1_1:
        cmp     al,11011b
        jne     nextkey_1_2
        mov     bl,'#'
nextkey_1_2:
        cmp     al,11101b
        jne     nextkey_1_3
        mov     bl,'&'
nextkey_1_3:

;----------------------------
nextsq2:
        cmp     ah,11101b
        jne     nextsq3

        cmp     al,01111b
        jne     nextkey_2_0
        mov     bl,'3'
nextkey_2_0:
        cmp     al,10111b
        jne     nextkey_2_1
        mov     bl,'7'
nextkey_2_1:
        cmp     al,11011b
        jne     nextkey_2_2
        mov     bl,'$'
nextkey_2_2:

;----------------------------
nextsq3:
        cmp     ah,11110b
        jne     nextsq4

        cmp     al,01111b
        jne     nextkey_3_0
        mov     bl,'4'
nextkey_3_0:
        cmp     al,10111b
        jne     nextkey_3_1
        mov     bl,'!'
nextkey_3_1:
        cmp     al,11011b
        jne     nextkey_3_2
        mov     bl,'%'
nextkey_3_2:
;----------------------------
nextsq4:
        mov     al,bl
        jmp     quit_pad_check

nothing_there:
        mov     al,0

quit_pad_check:
        ; no key was pressed, we quit the procedure. next time we will try
        ; next test sequence
        mov     bx,sentcount
        cmp     bx,3
        jne     increase_sq_counter
        mov     bx,0FFFFh

increase_sq_counter:
        inc     bx
        mov     sentcount,bx

        ret
;--------------------------------------------------------------------
quick_key_delay:
; changes the keyboard delays, so that it's easier to read
; them .. the keys come in faster
        push    bx
        push    ax
        mov     bx,1Eh
        mov     ah,3
        int     16h
        pop     ax
        pop     bx
        ret
;--------------------------------------------------------------------
slow_key_delay:
; Restores the original keyboard settings
        push    bx
        push    ax
        mov     bx,0310h
        mov     ah,3
        int     16h
        pop     ax
        pop     bx
        ret
;--------------------------------------------------------------------
set_colors:
; Initializes VGA colors pallete for the FX
        pusha
        mov     cx,1
        mov     dx,3C8H
        mov     bx,20
sc00:
        mov     al,cl
        out     dx,al
        inc     dx
        mov     al,bl
        out     dx,al
        mov     al,0
        out     dx,al
        mov     al,0
        out     dx,al
        dec     dx
        add     bx,3
        inc     cx
        cmp     cx,256
        jb      sc00
        popa
        ret
;--------------------------------------------------------------------
load_data_files:
; loads all the data files required to start up the program
        pusha

        ; clear the screen buffer:
        mov     bx,MAX_SCREEN_SIZE - 1
        mov     al,'$'
ldf01:
        mov     [screen + bx],al
        dec     bx
        cmp     bx,0
        jne     ldf01

        ; open the screen file for reading:
        lea     dx,screed_file
        mov     ax,3d00h
        int     21h
        mov     bx,ax
        ; read the screen file:
        lea     dx,screen
        mov     cx,MAX_SCREEN_SIZE
        mov     ax,3f00h
        int     21h
        ; close the screen file:
        mov     ax,3e00h
        int     21h

        popa
        ret
;--------------------------------------------------------------------
show_main_screen:
; displays the main program screen
        pusha

        ;kill the cursor .. it's ugly !!!
        mov     ah,1
        mov     cx,2000h
        int     10h

        ;clear the screen:
        lea     dx,line_feed
        mov     cx,MAX_LINES_NUMBER
        mov     ah,9
sms01:
        int     21h
        loop    sms01

        ; display the screen buffer:
        lea     dx,screen
        mov     ah,9
        int     21h
        popa
        ret
;--------------------------------------------------------------------
show_the_message:
; Displays the message under ds:di
        pusha

        ; Reposition the cursor:
        mov     ah,2
        mov     bh,0
        mov     dh,MSG_Y_POSITION
        mov     dl,MSG_X_POSITION
        int     10h

        ; Clear the line:
        lea     dx,msg_clear
        mov     ah,9
        int     21h

        ; Reposition the cursor:
        mov     ah,2
        mov     bh,0
        mov     dh,MSG_Y_POSITION
        mov     dl,MSG_X_POSITION
        int     10h

        ; Display the message:
        mov     dx,di
        mov     ah,9
        int     21h

        popa
        ret
;--------------------------------------------------------------------
start_recording:
; Initializes the recording mode
        pusha
        ; Inform the user about the operation
        lea     di,msg_recording
        call    show_the_message
        mov     al,MODE_RECORD
        mov     program_mode,al
        mov     ax,0
        mov     song_offset,ax

        ; Set the initial values for the first tune (0) and the initial
        ; system timer
        mov     ax,0
        mov     last_tone,ax
        int     1ah
        mov     last_timer,dx

        popa
        ret
;--------------------------------------------------------------------
stop_recording:
; Stops the recording mode and resets the right variables
        pusha

        ; Inform the user about the operation
        lea     di,msg_ready
        call    show_the_message

        ; Change the program mode back to ready
        mov     al,MODE_READY
        mov     program_mode,al

        ; Record the song size
        mov     bx,song_offset
        mov     song_size,bx

        ; Return to beginning of the song
        mov     ax,0
        mov     song_offset,ax

        ; Mark the end of the song in the buffer
        mov     ax,0FFFFh
        mov     [current_song+bx],ax
        mov     [current_song+2+bx],ax

        popa
        ret
;--------------------------------------------------------------------
decrease_bars:
; used by the FX procedure: changes the height of the
; vertical bars
        pusha

        mov     dx,1
        call    time_dalay_4FX

time_to_decrease:

        mov     bx,0a000h
        mov     es,bx

        mov     bx,14
db00:
        dec     bx
        mov     cl,[fx_bars+bx]
        mov     ch,0
        cmp     cl,0
        je      db01
        ; yes, we need to chop the bar off one row
        push    bx
        mov     ax,bx
        mov     bx,22
        mul     bx
        mov     di,ax
        mov     ax,BAR_TOP+1
        sub     ax,cx
        mov     cx,ax
        mov     ax,320
        mul     cx
        mov     bx,ax
        add     bx,di

        mov     cx,10
        mov     ax,0000h
db02:
        mov     es:[bx+320*0],ax
        mov     es:[bx+320*1],ax
        mov     es:[bx+320*2],ax
        mov     es:[bx+320*3],ax
        add     bx,2
        loop    db02
        pop     bx
        ; decrease the hight counter
        mov     cl,[fx_bars+bx]
        sub     cl,BAR_SPEED
        mov     [fx_bars+bx],cl

db01:
        cmp     bx,0
        jne     db00
        popa

        ret
;--------------------------------------------------------------------
time_dalay:
; waits 55m * DX seconds
; if we are in a FX mode, it also calls a function to
; play the FX
        ; save needed values
        pusha

        mov   bx,dx

        ; get the current timer value into di
        mov   ah,0
        int   1ah
        mov   di,dx
td01:
        pusha
        ; check if we are in the FX mode:
        mov     al,fx_mode
        cmp     al,TRUE
        jne     td02
        call    decrease_bars
td02:
        popa

        ; get the current timer value into dx
        mov   ah,0
        int   1ah
        sub   dx,di
        cmp   dx,bx
        jb    td01

        popa
        ret
;--------------------------------------------------------------------
time_dalay_4FX:
; waits 55m * DX seconds
; does not call any FX procedures, so that it
; can be used by them without any recursion
        ; save needed values
        pusha
        mov   bx,dx

        ; get the current timer value into di
        mov   ah,0
        int   1ah
        mov   di,dx
td01_4fx:
        ; get the current timer value into dx
        mov   ah,0
        int   1ah
        sub   dx,di
        cmp   dx,bx
        jb    td01_4fx

        popa
        ret
;--------------------------------------------------------------------
start_playing:
; Initializes the playing mode
        pusha

        ; check if we are in the FX mode:
        mov     al,fx_mode
        cmp     al,TRUE
        je      sp00

        ; Inform the user about the operation
        lea     di,msg_playing
        call    show_the_message

sp00:
        mov     bx,song_size
        cmp     bx,0
        je      stop_playing

        mov     bx,0
sp01:
        mov     cx,[current_song+bx]
        add     bx,2
        mov     dx,[current_song+bx]
        add     bx,2
        push    bx

        ; check if it is the silent tone (cx = 0)
        cmp     cx,0
        je      sp02

        ; play the sound
        in      al,61h
        or      al,11b
        out     61h,al

        mov     al,10110110b
        out     43h,al
        mov     al,cl
        out     42h,al
        mov     al,ch
        out     42h,al

        ; check if we are in the FX mode:
        mov     al,fx_mode
        cmp     al,TRUE
        jne     sp02
        ; yes, it's FX mode. Find out what bar to kick up:

check_tune1:
        cmp     cx,10847  ; here is the frequency
        jne     check_tune2
        mov     al,0
check_tune2:
        cmp     cx,9701  ; here is the frequency
        jne     check_tune3
        mov     al,1
check_tune3:
        cmp     cx,9108  ; here is the frequency
        jne     check_tune4
        mov     al,2
check_tune4:
        cmp     cx,8117  ; here is the frequency
        jne     check_tune5
        mov     al,3
check_tune5:
        cmp     cx,7231  ; here is the frequency
        jne     check_tune6
        mov     al,4
check_tune6:
        cmp     cx,6818  ; here is the frequency
        jne     check_tune7
        mov     al,5
check_tune7:
        cmp     cx,6088  ; here is the frequency
        jne     check_tune8
        mov     al,6
check_tune8:
        cmp     cx,5424  ; here is the frequency
        jne     check_tune9
        mov     al,7
check_tune9:
        cmp     cx,4850  ; here is the frequency
        jne     check_tune10
        mov     al,8
check_tune10:
        cmp     cx,4554  ; here is the frequency
        jne     check_tune11
        mov     al,9
check_tune11:
        cmp     cx,4058  ; here is the frequency
        jne     check_tune12
        mov     al,10
check_tune12:
        cmp     cx,3616  ; here is the frequency
        jne     check_tune13
        mov     al,11
check_tune13:
        cmp     cx,3409  ; here is the frequency
        jne     check_tune14
        mov     al,12
check_tune14:
        cmp     cx,3044  ; here is the frequency
        jne     check_tune15
        mov     al,13
check_tune15:
        ; now the al contains the number of the tone 0 .. 13
        ; we now kick the appropriate bar up:
        mov     bl,al
        mov     bh,0
        mov     ah,BAR_TOP
        mov     [fx_bars+bx],ah
        ; display the bar on the screen:
        call    bar_kick

sp02:
        call    time_dalay

        ; shuting the speaker off
        in al, 61h
        and al, 11111100b
        out 61h,al

        pop      bx
        cmp      bx,song_size
        jne      sp01

stop_playing:
        ; Inform the user about the operation
        lea     di,msg_ready
        call    show_the_message

        popa
        ret
;--------------------------------------------------------------------
bar_kick:
; Kicks a bar up depending on what key was played
; (used only in FX mode)
        pusha
        mov     bx,0a000h
        mov     es,bx

        mov     ah,0
        mov     si,ax
        add     si,1
        mov     bx,22
        mul     bx
        mov     di,ax
        mov     cx,BAR_TOP
bk01:
        mov     ax,320
        mul     cx
        mov     bx,ax
        add     bx,di

        push    cx
        mov     cx,10
        mov     ax,si
bk02:
        mov     es:[bx],ax ;
        add     bx,2
        loop    bk02
        pop     cx

        loop    bk01

        popa
        ret
;--------------------------------------------------------------------
start_playing_FX:
; Initializes everything that is needed for the FX playing mode
        pusha
        ; Inform the user about the operation

        mov     al,TRUE
        mov     fx_mode,al

        ; load graphical mode VGA (320x200x256)
        mov     ax,13h
        int     10h

        call    set_colors

        ; clear the screen:
        mov     ax,0a000h
        mov     es,ax
        mov     bx,320 * 200
        mov     al,0
spfx01:
        mov     es:[bx],al
        dec     bx
        jnz     spfx01

        ; reset FX bars:
        mov     bx,14
spfx02:
        dec     bx
        mov     [fx_bars+bx],al
        cmp     bx,0
        jnz     spfx02

        call    start_playing

        ; bring back the original screen:
        mov     ax,3h
        int     10h
        call    show_main_screen

        lea     di,msg_ready
        call    show_the_message

        ; reset all the values again:
        mov     al,FALSE
        mov     fx_mode,al
        popa
        ret
;--------------------------------------------------------------------
save_current_song:
; Saves the song present in the memory
; (either recorded or loaded)
        pusha
        ; Inform the user about the operation
        lea     di,msg_fnsave
        call    show_the_message

        ; Enter the filename
        lea     dx,song_file
        mov     ah,0ah
        int     21h

        ; Format the filename and get ready to save:
        mov     bl,[song_file+1]
        mov     bh,0
        lea     dx,song_file + 2
        mov     al,0
        mov     [song_file+2+BX],al

        ;Save the song buffer into the filename:

        mov     ax,3c00h
        mov     cx,0
        int     21h
        jc      scs_error_found
        mov     bx,ax
        ; write the buffer:
        lea     dx,current_song
        mov     cx,4 * MAX_SONG_LEN
        mov     ax,4000h
        int     21h
        jc      scs_error_found
        ; close the file:
        mov     ax,3e00h
        int     21h
        jc      scs_error_found

        ; Inform the user about the operation
        lea     di,msg_saved
        call    show_the_message
scs_exit:
        popa
        ret
scs_error_found:
        lea     di,msg_error
        call    show_the_message
        jmp     scs_exit
;--------------------------------------------------------------------
load_a_song:
; Loads a new song into the memory, overwriting any existing
; song data
        pusha
        ; Inform the user about the operation
        lea     di,msg_fnload
        call    show_the_message

        ; Enter the filename
        lea     dx,song_file
        mov     ah,0ah
        int     21h

        ; Format the filename and get ready to load:
        mov     bl,[song_file+1]
        mov     bh,0
        lea     dx,song_file + 2
        mov     al,0
        mov     [song_file+2+BX],al

        ;Open the song file:
        mov     ax,3d00h
        int     21h
        jc      las_error_found
        mov     bx,ax
        ; write the buffer:
        lea     dx,current_song
        mov     ax,3f00h
        mov     cx,4 * MAX_SONG_LEN
        int     21h
        jc      las_error_found
        ; close the file:
        mov     ax,3e00h
        int     21h
        jc      las_error_found

        ; Start looking for the marked end of the buffer (two dw FFFFs)
        mov     bx, 2 * MAX_SONG_LEN - 4
        mov     song_size,bx
        mov     bx,0
las01:
        cmp     [current_song+bx],0FFFFh
        jne     las02
        cmp     [current_song+2+bx],0FFFFh
        jne     las02
        ; Found it at [+bx]:
        mov     song_size,bx
        jmp     las03
las02:
        add     bx,4
        cmp     bx, 2 * MAX_SONG_LEN - 4
        jne     las01
las03:

        ; Inform the user about the operation
        lea     di,msg_loaded
        call    show_the_message
las_exit:
        popa
        ret
las_error_found:
        lea     di,msg_error
        call    show_the_message
        jmp     las_exit

;--------------------------------------------------------------------
play_tone:
; causes the speaker to play a tone (depending on pressed key)
; for certain time, and then quits
        pusha

        ; figure out the frequency:
        cmp     al,'1'
        jne     skip_label1
        mov     dl,2 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,10847  ; here is the frequency
skip_label1:
        cmp     al,'2'
        jne     skip_label2
        mov     dl,6 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,9701  ; here is the frequency
skip_label2:
        cmp     al,'3'
        jne     skip_label3
        mov     dl,10 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,9108  ; here is the frequency
skip_label3:
        cmp     al,'4'
        jne     skip_label4
        mov     dl,14 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,8117  ; here is the frequency
skip_label4:
        cmp     al,'5'
        jne     skip_label5
        mov     dl,18 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,7231  ; here is the frequency
skip_label5:
        cmp     al,'6'
        jne     skip_label6
        mov     dl,22 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,6818  ; here is the frequency
skip_label6:
        cmp     al,'7'
        jne     skip_label7
        mov     dl,26 ; x of the finger
        mov     dh,8 ; y of the finger
        mov     cx,6088  ; here is the frequency
skip_label7:
        cmp     al,'!'
        jne     skip_label1s
        mov     dl,2 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,5424  ; here is the frequency
skip_label1s:
        cmp     al,'@'
        jne     skip_label2s
        mov     dl,6 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,4850  ; here is the frequency
skip_label2s:
        cmp     al,'#'
        jne     skip_label3s
        mov     dl,10 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,4554  ; here is the frequency
skip_label3s:
        cmp     al,'$'
        jne     skip_label4s
        mov     dl,14 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,4058  ; here is the frequency
skip_label4s:
        cmp     al,'%'
        jne     skip_label5s
        mov     dl,18 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,3616  ; here is the frequency
skip_label5s:
        cmp     al,'^'
        jne     skip_label6s
        mov     dl,22 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,3409  ; here is the frequency
skip_label6s:
        cmp     al,'&'
        jne     skip_label7s
        mov     dl,26 ; x of the finger
        mov     dh,17 ; y of the finger
        mov     cx,3044  ; here is the frequency
skip_label7s:

        ; memorize the finger position:
        mov     finger_x, dl
        mov     finger_y, dh

        ; put the finger sign on the screen:
        mov     di,4
pt04:
        mov     ah,2
        mov     bh,0
        int     10h
        mov     ah,2
        push    dx
        mov     dl,FINGER_CHARACTER
        int     21h
        int     21h
        int     21h
        pop     dx
        dec     dh
        dec     di
        jnz     pt04

        ; play the sound
        in      al,61h
        or      al,11b
        out     61h,al

        mov     al,10110110b
        out     43h,al
        mov     al,cl
        out     42h,al
        mov     al,ch
        out     42h,al

        ; Check if the program is currently recording:
        mov     al,program_mode
        cmp     al,MODE_RECORD
        jne     not_recording
        ; Yap,  save last tune and the timer
        mov     ax,last_tone
        mov     bx, song_offset
        mov     [current_song+bx],ax
        ; Remember the new tune
        mov     last_tone,cx
        ; Get time durance of the last tone:
        push    dx
        push    cx
        mov     ah,0
        int     1ah
        mov     cx,last_timer
        mov     last_timer, dx
        sub     dx,cx
        mov     ax,dx
        pop     cx
        pop     dx
        mov     [current_song+bx+2],ax
        add     bx,4
        mov     song_offset, bx

not_recording:

pt01:
        ; cause a small time delay:
        mov     dx,6 ; Wait 6/18 sec, so that we get
                     ; the next key that comes in every 1/4 th sec
        call    time_dalay_4FX

        ; check if the key is still pressed:
        mov     ah,1
        int     16h
        jz      pt02 ; no, let's shut the speaker off
        ; yes, let's clear it from the buffer, and check again:
        mov     ah,0
        int     16h
        jmp     pt01

pt02:
        ; shuting the speaker off
        in al, 61h
        and al, 11111100b
        out 61h,al

        ; take the finger sign off the screen:
        mov     dh,finger_y
        mov     dl,finger_x
        mov     di,4
pt05:
        mov     ah,2
        mov     bh,0
        int     10h
        mov     ah,2
        push    dx
        mov     dl,SPACE_CHARACTER
        int     21h
        int     21h
        int     21h
        pop     dx
        dec     dh
        dec     di
        jnz     pt05

        ; Check if the program is currently recording .. again...
        mov     al,program_mode
        cmp     al,MODE_RECORD
        jne     not_recording2
        ; Yap,  save last tune and the timer
        mov     ax,last_tone
        mov     bx, song_offset
        mov     [current_song+bx],ax
        ; Remember the new tone .. silent
        mov     cx,0
        mov     last_tone,cx
        ; Get time durance of the last tone:
        push    dx
        push    cx
        mov     ah,0
        int     1ah
        mov     cx,last_timer
        mov     last_timer, dx
        sub     dx,cx
        mov     ax,dx
        pop     cx
        pop     dx
        mov     [current_song+bx+2],ax
        add     bx,4
        mov     song_offset, bx

not_recording2:

        popa
        ret
;--------------------------------------------------------------------
; End of the program
end program_start

