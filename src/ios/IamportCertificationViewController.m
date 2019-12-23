//
//  IamportCertificationViewController.m
//  IamportCordovaExample
//
//  Created by deedee on 2019/12/23.
//

#import "IamportCertificationViewController.h"

@implementation IamportCertificationViewController: IamportViewController

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    NSLog(@"url: %@", @"webVIewDidFinishLoad");
    if (super.isWebViewLoaded == NO) {
        NSLog(@"url: %@", @"isWebViewLoaded");
        [super.webView stringByEvaluatingJavaScriptFromString:@"IMP.init('imp10391932');"];
        [super.webView stringByEvaluatingJavaScriptFromString:@"IMP.certification({ 'amount': '1000' });"];
        super.isWebViewLoaded = YES;
    }
}

@end
