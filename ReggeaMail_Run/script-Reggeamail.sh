#*****************************************************************************
#* 				  				REAGGEAMAIL 								 *
#*****************************************************************************
#* @File : script-Reggeamail.sh 		 	                               	 *
#* @By : NGL							 				                     *
#*****************************************************************************/



#!/bin/bash

#on recupere le chemin courant:


# copie de ta contab actuelle : 
crontab -l > /tmp/ma_crontab

# ajoutes de la commande : 

echo "*/1 * * * * java -jar $PWD/ReggeaMail.jar >> Reaggeamail.log" >> /tmp/ma_crontab


# on replaces ta crontab : 
crontab /tmp/ma_crontab

# on supprimes ton fichier temporaire : 
rm -f /tmp/ma_crontab
