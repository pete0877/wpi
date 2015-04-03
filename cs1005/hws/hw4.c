
#include <stdio.h>
#include <string.h>
#include <ctype.h>

     void
     get_word(char * word);

     void
     convert_word(char * word);

     int
     lookup(char * word, char table[][21], int nwords,int numb[]);

     void
     insert_word(char * word, char table[][21], int position );

     char
     *rev_word(char *in, char *out);

     void
     print_table(char table[][21], int nwords, int numb[]);

     char
     *sort(char table[][21], int numb[], int n);


int main (void)
{
 	
	char  s[21],             /* a temp word variable */
	      table[200][21];    /* the table of words   */
	int ns[200];             /* array of occurences  */
        int n;                   /* count variable       */

   for(n=0;n<199;n++) ns[n]=1;   /*   set up the array   */
   n=0;
   do
   {
   	get_word(s);    /*  input a word from the inout */

      if (!strcmp(s,"* no more *"))
      {
	/*    If it is the end of the file : */
	print_table(sort(table,ns,n),n,ns);
      	return(0);
      }
      convert_word(s);   /* make the word loower case */
      if (!lookup(s,table,n,ns))
      {
	/*   word is not in the table yet   */
      	insert_word(s,table,n);
      	n++;
      }
   } while (1);
   /* wont quit until the end of file is reached  */
}


  /*  Aquires word form the standard input */
  /*  or it returns "* no more *" if there */
  /*  is no more words                     */
void
get_word(char * word)
{
	int ok=1;
   char z;
	do
   {
   	z=getc(stdin);
      if (z==EOF)
      {
      	strcpy(word,"* no more *");
         ok=0;
      }
      else
       if (isalpha(z))
       {
      	ungetc(z,stdin);
         scanf("%s",word);
         ok=0;
       }
   } while (ok);
}

   /*  Convers the given word to the lower case */
void
convert_word(char * word)
{
	int a;
	for (a=0; a<strlen(word); a++) word[a]=tolower(word[a]);
}

   /* Returns 1 if the word is in the table already, */
   /* otherwise it returns 0                         */
int
lookup(char * word, char table[][21], int nwords,int numb[])
{
	int a,is=0;
   for (a=0; a<nwords; a++)
   	if (strcmp(word,table[a])==0)
   {
   	is=1;
      numb[a]++;
   }
   return(is);
}

   /* inserts word into the table   */
void
insert_word(char * word, char table[][21], int position )
{	strcpy(table[position],word); }

   /* returns addres to a reversed word */
char *rev_word(char *in, char *out)
{
   int a;
   for (a=0; a<strlen(in); a++) out[a]=in[strlen(in)-1-a];
   out[strlen(in)]='\0';
	return(out);
}

   /*  prints out info table as a final step of the program */
void
print_table(char table[][21], int nwords, int numb[])
{
	int a;
   char srev[21];
   printf(" \n\n  Here is list of SORTED and then REVERSED words : \n\n");
   printf("****************************************************\n");
   printf("* %-3s * %-21s * %-8s * %-7s *\n","#","Reversed Word","#Ocured","Length");
   printf("****************************************************\n");
	for (a=0;a<nwords;a++)
 	 printf ("* %-3d * %-21s * %-8d * %-7d *\n",a+1,rev_word(table[a],srev),numb[a],strlen(table[a]));
   printf("****************************************************\n");

}

    /* sorts the table and rearanges array of occurences */
char *sort(char table[][21], int numb[],int n)
{
	int a,b,p,t;
   char s[21];
   for (a=0; a<n; a++)
   {
		strcpy(s,table[a]);
      p=a;
   	for (b=a; b<n; b++)
      	if (strcmp(table[b],s)<0)
         {
         	p=b;
            strcpy(s,table[b]);
         }
      strcpy(table[p],table[a]);
      strcpy(table[a],s);
      t=numb[a];
      numb[a]=numb[p];
      numb[p]=t;
   }
	return(table);
}

