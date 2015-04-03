#include <stdio.h>

void main (void)
{

	int n,a,b;
	int t[20];	
	do
	{
		printf("Input n: ");
		scanf("%d",&n);
	} while (n<1 || n>20);

	printf("Input the list: ");
	for (a=1;a<=n;a++) scanf("%d",&t[a]);

	printf("The list entered: ");
	for (a=1;a<=n;a++) printf("%d ",t[a]);

	for (a=1;a<=n;a++) 
	{
		printf("\n%4d ",t[a]);
		for (b=1;b<=t[a];b++) printf("*");
	}
	printf("\n\n");
}
