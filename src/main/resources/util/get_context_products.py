# encoding: utf-8
#
# Copyright © 2019, California Institute of Technology ("Caltech").
# U.S. Government sponsorship acknowledged.
#
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# • Redistributions of source code must retain the above copyright notice,
#   this list of conditions and the following disclaimer.
# • Redistributions must reproduce the above copyright notice, this list of
#   conditions and the following disclaimer in the documentation and/or other
#   materials provided with the distribution.
# • Neither the name of Caltech nor its operating division, the Jet Propulsion
#   Laboratory, nor the names of its contributors may be used to endorse or
#   promote products derived from this software without specific prior written
#   permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

'''
Python Script that writes out all the registered Context LIDVIDs from a given Search Service
onto an output file. The intent is that this output file can then be fed into the Validate Tool
to check that Context references in a given Product Label exists in the Registry.

@author: mcayanan
'''
from __future__ import print_function

from urllib2 import *
import json
import argparse
import logging
import traceback
import sys


SEARCH_URL = 'https://pds.nasa.gov/services/search'
OUTPUT_FILE_NAME = 'registered_context_products.json'
ENDPOINT = "archive-filter"
QUERY = "q=product_class:Product_Context&wt=json"

# Set up logging
logging.basicConfig(level=logging.INFO)
LOGGER = logging.getLogger(__name__)

def get_parser():
    '''
    Get a parser for this application
    @return: parser to for this application
    '''
    parser = argparse.ArgumentParser(description="Gets the latest Context Products from a given Registry")
    parser.add_argument("--search_url", nargs=1, required=False, help="Specify the Search Service URL to look for Context Products. Default is to go to {}.".format(SEARCH_URL))
    parser.add_argument("--output", nargs=1, required=False, help="Specify an output file to write the results. Default is to write to a file named '{}'.".format(OUTPUT_FILE_NAME))
    
    return parser

def get_context_products(search_url, output_file):
    query_url = "{}/{}/?{}".format(search_url,ENDPOINT,QUERY)
    connection = urlopen(query_url)
    response = json.load(connection)
    total_results = response['response']['numFound']
    LOGGER.info("{} documents found. Getting the LIDs/LIDVIDs...".format(total_results))

    #f = open(output_file,"w+")

    # Write the LIDVID to the output file for each document found
    counter = 0
    registered_products = {"Product_Context":[]}
    while len(response['response']['docs']) != 0:
        for document in response['response']['docs']:
            registered_products['Product_Context'].append("{}::{}".format(document['identifier'], str(document['version_id'][0])))
            counter = counter + 1
        LOGGER.info("Processed {} documents".format(counter))
        connection = urlopen("{}&start={}&rows=100".format(query_url,counter))
        response = json.load(connection)
        
    with open(output_file, "w+") as f:
        json.dump(registered_products, f, indent=2, sort_keys=True)
    

def main():
    '''
    Main entry point
    '''
    search_url = SEARCH_URL
    output_file = OUTPUT_FILE_NAME
    args = get_parser().parse_args()
    
    if args.search_url != None:
        search_url = args.search_url[0]
        
    if args.output != None:
        output_file = args.output[0]
            
    get_context_products(search_url, output_file)
    LOGGER.info("{} has been created.".format(output_file))
    
if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        LOGGER.error(traceback.format_exc())
        sys.exit(1)