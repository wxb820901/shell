Meta:  
@author demo
@theme  demo
  
Narrative: demo
  
Scenario: Bond Price got changed by 10% and Calc Engine generated an alert

Given a Bond with a threshold setting for DoD 0.1

When 1, BondPrice for yesterday is 100 and today is 110
Then 1, alert should be 10

When 2, BondPrice for yesterday is 110 and today is 110
Then 2, alert should be 'There is no alert'

When 3, BondPrice for yesterday is 100 and today is 109
Then 3, alert should be 'there is no alert'


