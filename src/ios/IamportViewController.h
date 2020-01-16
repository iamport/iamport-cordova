//
//  IamportViewController.h
//  IamportCordovaExample
//
//  Created by deedee on 2019/12/19.
//

#import <UIKit/UIKit.h>
#import <Cordova/CDVCommandDelegate.h>
#import "IamportCordova.h"

@interface IamportViewController: UIViewController<UIWebViewDelegate, IamportDelegate>

@property () IamportCordova* delegate;
@property (strong, nonatomic) IBOutlet UIWebView *webView;
@property () BOOL isWebViewLoaded;
@property (strong, nonatomic) NSString *callbackId;
@property (nonatomic, weak) id <CDVCommandDelegate> commandDelegate;
@property (strong, nonatomic) NSString *type;
@property (strong, nonatomic) NSObject *titleData;
@property (strong, nonatomic) NSObject *params;

- (id) init;
- (void) onDidReceiveData:(NSNotification *)notification;
- (void) showIframe:(NSString*) userCode data:(NSString*)data triggerCallback:(NSString*)triggerCallback;
- (BOOL) isOver:(NSString*) url;
- (BOOL) isUrlStartsWithAppScheme:(NSString *)url;
- (void) openThirdPartyApp:(NSString *)url;
- (NSString*) toJsonString:(NSObject*) object;

@end
