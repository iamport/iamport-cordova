//
//  IamportViewController.m
//  IamportCordovaExample
//
//  Created by deedee on 19/12/2019.
//

#import "IamportViewController.h"

@implementation IamportViewController: UIViewController

- (id)init
{
    if (self = [super init]) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onDidReceiveData:) name:CDVPluginHandleOpenURLNotification object:nil];
    }
    
    return self;
}

- (void)loadView
{
    [super loadView];

    _isWebViewLoaded = NO;

    _webView = [[WKWebView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    _webView.UIDelegate = self;
    _webView.navigationDelegate = self;

    self.view = _webView;
    
    _delegate = [[IamportCordova alloc] init];
    [_delegate setDelegate:self];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"iamport-webview" ofType:@"html" inDirectory:@"www"];
    NSString* htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSUTF8StringEncoding error:nil];
    [_webView loadHTMLString:htmlString baseURL:[[NSBundle mainBundle] bundleURL]];
}

- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(nonnull WKNavigationAction *)navigationAction decisionHandler:(nonnull void (^)(WKNavigationActionPolicy))decisionHandler
{
    NSString *url = navigationAction.request.URL.absoluteString;
    
    if ([self isOver:url]) { // 결제완료
        /*
         웹뷰 해제
         */
        [_webView stopLoading];
        [_webView removeFromSuperview];
        _webView.UIDelegate = nil;
        _webView.navigationDelegate = nil;
        
        _webView = nil;
       
        
        /*
         Notification 해제
         */
        [[NSNotificationCenter defaultCenter] removeObserver:self name:CDVPluginHandleOpenURLNotification object:nil];
        
        /*
         delegate 메소드 호출
         */
        [self dismissViewControllerAnimated:YES completion:nil];
        [_delegate onOver:url callbackId:_callbackId commandDelegate:_commandDelegate];
        
        decisionHandler(WKNavigationActionPolicyCancel);
    } else if ([self isUrlStartsWithAppScheme:url]) { // 외부 앱 호출
        [self openThirdPartyApp:url];
        
        decisionHandler(WKNavigationActionPolicyCancel);
    } else {
        decisionHandler(WKNavigationActionPolicyAllow);
    }
}

- (void)webView:(WKWebView *)webView didFinishNavigation:(null_unspecified WKNavigation *)navigation
{
        if (_isWebViewLoaded == NO) {
            NSString* userCode = [_params valueForKey:@"userCode"];
            NSString* data = [self toJsonString:[_params valueForKey:@"data"]];
            NSString* triggerCallback = [_params valueForKey:@"triggerCallback"];
            [self showIframe: userCode data:data triggerCallback:triggerCallback];
            
            _isWebViewLoaded = YES;
        }
}

- (void)webView:(WKWebView *)webView runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(void))completionHandler
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:message message:nil preferredStyle:UIAlertControllerStyleAlert];
    [alertController addAction:[UIAlertAction actionWithTitle:@"닫기" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
        completionHandler();
    }]];
    [self presentViewController:alertController animated:YES completion:^{}];
}

- (void)webView:(WKWebView *)webView runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(BOOL result))completionHandler
{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:message preferredStyle:UIAlertControllerStyleAlert];
    
    [alert addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"취소", nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
        completionHandler(NO);
    }]];
    
    [alert addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"확인", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        completionHandler(YES);
    }]];
    
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)onDidReceiveData:(NSNotification *)notification
{
    
}

- (void)showIframe: (NSString*)userCode data:(NSString*)data triggerCallback:(NSString*)triggerCallback
{
    
}

- (BOOL)isOver:(NSString *) url
{
    /*
     웹뷰가 포트원가 지정한 임의의 m_redirect_url과 같으면 결제 종료(완료 또는 실패)로 판단
     */
    NSString* redirectUrl = [_params valueForKey:@"redirectUrl"];
    return [url hasPrefix:redirectUrl];
}

- (BOOL)isUrlStartsWithAppScheme:(NSString *)url
{
    return ![url hasPrefix:@"http"] && ![url hasPrefix:@"https"] && ![url hasPrefix:@"about:blank"] && ![url hasPrefix:@"file"];
}

- (void)openThirdPartyApp:(NSString *)url
{
    
}

- (NSString*) toJsonString:(NSObject*) object
{
    /*
     NSObject를 json string으로 변환
     */
    NSError *writeError = nil;

    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:object options:NSJSONWritingPrettyPrinted error:&writeError];
    NSString *stringData = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    return stringData;
}

@end
