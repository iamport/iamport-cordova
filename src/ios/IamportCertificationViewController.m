//
//  IamportCertificationViewController.m
//  IamportCordovaExample
//
//  Created by deedee on 2019/12/23.
//

#import "IamportCertificationViewController.h"

@implementation IamportCertificationViewController: IamportViewController

- (void)showIframe: (NSString*)userCode data:(NSString*)data triggerCallback:(NSString*)triggerCallback
{
    NSString *initCommand = [NSString stringWithFormat: @"IMP.init('%@');", userCode];
    NSString *requestCommand = [NSString stringWithFormat: @"IMP.certification(%@, %@);", data, triggerCallback];
    
    UIWebView* webView = [self valueForKey:@"webView"];
    [webView stringByEvaluatingJavaScriptFromString:initCommand];
    [webView stringByEvaluatingJavaScriptFromString:requestCommand];
}

@end
