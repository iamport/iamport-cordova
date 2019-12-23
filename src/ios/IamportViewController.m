//
//  IamportViewController.m
//  IamportCordovaExample
//
//  Created by deedee on 19/12/2019.
//

#import "IamportViewController.h"

@implementation IamportViewController: UIViewController

- (void)loadView {
    [super loadView];

    _isWebViewLoaded = NO;

    _webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    _webView.delegate = self;
    
    self.view = _webView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"iamport-webview" ofType:@"html" inDirectory:@"www"];
    NSString* htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSUTF8StringEncoding error:nil];
    [_webView loadHTMLString:htmlString baseURL:[[NSBundle mainBundle] bundleURL]];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(nonnull NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSString *url = request.URL.absoluteString;
    NSLog(@"url: %@", url);
    
    if ([self isOver:url]) {
        
    }
    return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    NSLog(@"url: %@", @"webVIewDidFinishLoad");
    if (_isWebViewLoaded == NO) {
        NSLog(@"url: %@", @"isWebViewLoaded");
        NSString* userCode = [_params valueForKey:@"userCode"];
        
        [_webView stringByEvaluatingJavaScriptFromString:@"IMP.init('iamport');"];
        [_webView stringByEvaluatingJavaScriptFromString:@"IMP.request_pay({ 'amount': '1000' });"];
        _isWebViewLoaded = YES;
    }
}

- (BOOL)isOver:(NSString *) url {
    return YES;
}

@end
