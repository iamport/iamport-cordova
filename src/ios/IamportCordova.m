/********* IamportCordova.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "IamportCordova.h"
#import "IamportPaymentViewController.h"
#import "IamportInicisViewController.h"
#import "IamportNiceViewController.h"
#import "IamportCertificationViewController.h"

@implementation IamportCordova {
}

- (void)startActivity: (CDVInvokedUrlCommand*)command
{
    NSString* type = [command.arguments objectAtIndex:0];
    NSObject* titleData = [command.arguments objectAtIndex:1];
    NSObject* params = [command.arguments objectAtIndex:2];

    IamportViewController *iamportViewController = nil;
    if ([type isEqualToString:@"nice"]) {
        iamportViewController = [[IamportNiceViewController alloc] init];
    } else if ([type isEqualToString:@"inicis"]) {
        iamportViewController = [[IamportInicisViewController alloc] init];
    } else if ([type isEqualToString:@"certification"]) {
        iamportViewController = [[IamportCertificationViewController alloc] init];
    } else {
        iamportViewController = [[IamportPaymentViewController alloc] init];
    }

    iamportViewController.type = type;
    iamportViewController.titleData = titleData;
    iamportViewController.params = params;
    /*
     delegate 메소드에 전달
     delegate 메소드에서 self.commandDelegate와 command.callbackId에 접근할 수 없는 점 방지
     */
    iamportViewController.callbackId = command.callbackId;
    iamportViewController.commandDelegate = self.commandDelegate;

    [self.viewController presentViewController:iamportViewController animated:YES completion:nil];
}

- (void)setDelegate:(id<IamportDelegate>)delegate
{
    _delegate = delegate;
}

- (void)onOver:(NSString*)url callbackId:(NSString *)callbackId commandDelegate: (id<CDVCommandDelegate>)commandDelegate
{
    /*
     결제완료 후 CDV에 결제 결과 URL 전달
     */
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:url];
    [commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

@end
