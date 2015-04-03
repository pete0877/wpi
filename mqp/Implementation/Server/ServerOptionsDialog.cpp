// ServerOptionsDialog.cpp : implementation file
//

#include "stdafx.h"
#include "DistributedGameTreeServer.h"
#include "ServerOptionsDialog.h"
#include "StringTool.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// ServerOptionsDialog dialog


ServerOptionsDialog::ServerOptionsDialog(CWnd* pParent /*=NULL*/)
	: CDialog(ServerOptionsDialog::IDD, pParent)
{
	//{{AFX_DATA_INIT(ServerOptionsDialog)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}

void ServerOptionsDialog::setOptions(const GameServerConfiguration& newOptions)
{
	options = newOptions;
}

GameServerConfiguration ServerOptionsDialog::getOptions()
{
	return options;
}


void ServerOptionsDialog::update() 
{
	CEdit*	textbox;
	char	tmp[100];

	textbox = (CEdit*) GetDlgItem(IDC_EDIT1);
	textbox->SetWindowText(options.getServerName().GetBuffer(0));

	textbox = (CEdit*) GetDlgItem(IDC_EDIT2);
	textbox->SetWindowText(options.getServerOwner().GetBuffer(0));

	textbox = (CEdit*) GetDlgItem(IDC_EDIT3);
	textbox->SetWindowText(options.getServerDescription().GetBuffer(0));

	textbox = (CEdit*) GetDlgItem(IDC_EDIT4);	
	sprintf(tmp, "%d", options.getPort());
	textbox->SetWindowText(tmp);	

	textbox = (CEdit*) GetDlgItem(IDC_EDIT5);	
	sprintf(tmp, "%d", options.getMaxClients());
	textbox->SetWindowText(tmp);

	textbox = (CEdit*) GetDlgItem(IDC_EDIT6);	
	sprintf(tmp, "%d", options.getMaxClientsPerGameType());
	textbox->SetWindowText(tmp);

	textbox = (CEdit*) GetDlgItem(IDC_EDIT7);	
	sprintf(tmp, "%d", options.getMaxDecisionTime());
	textbox->SetWindowText(tmp);

	textbox = (CEdit*) GetDlgItem(IDC_EDIT8);	
	sprintf(tmp, "%d", options.getMaxGamePly());
	textbox->SetWindowText(tmp);
}

void ServerOptionsDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(ServerOptionsDialog)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(ServerOptionsDialog, CDialog)
	//{{AFX_MSG_MAP(ServerOptionsDialog)
	ON_EN_CHANGE(IDC_EDIT1, OnChangeEdit1)
	ON_EN_CHANGE(IDC_EDIT2, OnChangeEdit2)
	ON_EN_CHANGE(IDC_EDIT3, OnChangeEdit3)
	ON_EN_CHANGE(IDC_EDIT4, OnChangeEdit4)
	ON_EN_CHANGE(IDC_EDIT5, OnChangeEdit5)
	ON_EN_CHANGE(IDC_EDIT6, OnChangeEdit6)
	ON_EN_CHANGE(IDC_EDIT7, OnChangeEdit7)
	ON_EN_CHANGE(IDC_EDIT8, OnChangeEdit8)	
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// ServerOptionsDialog message handlers


void ServerOptionsDialog::OnChangeEdit1() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT1);
	textbox->GetWindowText(s);
	options.setServerName(s);	
}

void ServerOptionsDialog::OnChangeEdit2() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT2);
	textbox->GetWindowText(s);
	options.setServerOwner(s);	
}

void ServerOptionsDialog::OnChangeEdit3() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT3);
	textbox->GetWindowText(s);
	options.setServerDescription(s);	
}

void ServerOptionsDialog::OnChangeEdit4() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT4);
	textbox->GetWindowText(s);
	long value=0;
	StringTool* str = new StringTool();			
	try {	
		str->set(s);
		value = str->toLong();		
		options.setPort(value);
	} catch (CException* e) { e->Delete(); }
	delete str;	
}

void ServerOptionsDialog::OnChangeEdit5() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT5);
	textbox->GetWindowText(s);
	long value=0;
	StringTool* str = new StringTool();			
	try {	
		str->set(s);
		value = str->toLong();		
		if (value>0) options.setMaxClients(value);
	} catch (CException* e) { e->Delete(); }
	delete str;		
}

void ServerOptionsDialog::OnChangeEdit6() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT6);
	textbox->GetWindowText(s);
	long value=0;
	StringTool* str = new StringTool();			
	try {	
		str->set(s);
		value = str->toLong();		
		if (value>0) options.setMaxClientsPerGameType(value);
	} catch (CException* e) { e->Delete(); }
	delete str;	
}

void ServerOptionsDialog::OnChangeEdit7() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT7);
	textbox->GetWindowText(s);
	long value=0;
	StringTool* str = new StringTool();			
	try {	
		str->set(s);
		value = str->toLong();		
		if (value>0) options.setMaxDecisionTime(value);
	} catch (CException* e) { e->Delete(); }
	delete str;	
}

void ServerOptionsDialog::OnChangeEdit8() 
{
	CString s;
	CEdit* textbox = (CEdit*) GetDlgItem(IDC_EDIT8);
	textbox->GetWindowText(s);
	long value=0;
	StringTool* str = new StringTool();			
	try {	
		str->set(s);
		value = str->toLong();		
		if (value>0) options.setMaxGamePly(value);
	} catch (CException* e) { e->Delete(); }
	delete str;	
}

BOOL ServerOptionsDialog::OnInitDialog() 
{
	CDialog::OnInitDialog();
	this->update();
	// TODO: Add extra initialization here
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

