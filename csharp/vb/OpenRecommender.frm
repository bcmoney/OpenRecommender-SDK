VERSION 5.00
Begin VB.Form OpenRecommender 
   Caption         =   "OpenRecommender"
   ClientHeight    =   4050
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   4710
   LinkTopic       =   "OpenRecommender"
   ScaleHeight     =   1050
   ScaleWidth      =   4710
   StartUpPosition =   3  'Windows Default
   Begin VB.Timer Timer1 
      Interval        =   1000
      Left            =   3960
      Top             =   540
   End
   Begin VB.CommandButton recommendButton 
      Caption         =   "Recommended For You"
      Height          =   735
      Left            =   480
      TabIndex        =   0
      Top             =   120
      Width           =   3735
   End
End
Attribute VB_Name = "OpenRecommender"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
'Very simple hello world program.
'Illustrates a command button and a timer.
'The command button is named recommendButton in the Properties frame,
' and is caption is set to Hello World
'The timer properties are set as we load the form, but
'  we could also have done that in the properties frame.
'When the button is pressed, the timer is enabled and it counts down
' from 5 seconds to 0, and then destroys itself.

Option Explicit              'VB will now warn us if a variable is not declared
Dim ElapsedTime As Integer   'Global variable for how much time has passed


'XML Parser setup
Dim xml_document As New DOMDocument
Dim xml_node As IXMLDOMNode

Private Sub getRecommendations()
	' download xml from server
	debugXML.Text = inet.OpenURL("recommendations.xml", icString)
	' parse as xml
	xml_document.loadXML debugXML.Text
End Sub


'The Form_Load event is raised when the interface is created.
'We just initialize some things here.
Private Sub Form_Load()
    OpenRecommender.Caption = "OpenRecommender - Recommendation Listing"
    ElapsedTime = 5000
    Timer1.Enabled = False
End Sub

'The click event is raised when the button is pressed
Private Sub recommendButton_Click()
    Timer1.Interval = 1   'We set it to a very fast interval
    Timer1.Enabled = True 'and then enable it to make it fire immediately
    Timer1.Interval = 1000 'subsequent timer ticks will be 1 second apart
    recommendButton.Enabled = False 'The button can no longer be clicked
End Sub

'This event is raised on every tick of the timer.
Private Sub Timer1_Timer()
     ElapsedTime = ElapsedTime - Timer1.Interval
     recommendButton.Caption = "Goodbye in " & ElapsedTime / 1000 & " seconds)"
     If ElapsedTime <= 0 Then End
End Sub
